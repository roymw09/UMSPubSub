package org.ac.cst8277.williams.roy.controller;

import org.ac.cst8277.williams.roy.model.Publisher;
import org.ac.cst8277.williams.roy.model.Subscriber;
import org.ac.cst8277.williams.roy.model.User;
import org.ac.cst8277.williams.roy.model.UserRole;
import org.ac.cst8277.williams.roy.service.RoleService;
import org.ac.cst8277.williams.roy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.ReactiveSubscription;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import javax.annotation.PostConstruct;

@RestController
@RequestMapping("/users/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserService userService;


    @Autowired
    private ReactiveRedisOperations<String, Publisher> publisherTokenTemplate;

    @Autowired
    private ReactiveRedisOperations<String, Subscriber> subscriberTokenTemplate;

    @PostConstruct
    private void initPublisherTokenReceiver() {
        this.publisherTokenTemplate
                .listenTo(ChannelTopic.of("publisher_token"))
                .map(ReactiveSubscription.Message::getMessage).subscribe(publisher -> {
                    UserRole userRole = new UserRole(publisher.getId(), "PUBLISHER", "Message content producer");
                    savePublisherToken(userRole).subscribe();
                });
    }

    @PostConstruct
    private void initSubscriberTokenReceiver() {
        this.subscriberTokenTemplate
                .listenTo(ChannelTopic.of("subscriber_token"))
                .map(ReactiveSubscription.Message::getMessage).subscribe(subscriber -> {
                    UserRole userRole = new UserRole(subscriber.getId(), "SUBSCRIBER", "Message content consumer");
                    saveSubscriberToken(userRole).subscribe();
                });
    }

    @PostMapping("/token/savePublisher")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<UserRole> savePublisherToken(@RequestBody UserRole userRole) {
        return roleService.savePublisherToken(userRole);
    }

    @PostMapping("/token/saveSubscriber")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<UserRole> saveSubscriberToken(@RequestBody UserRole userRole) {
        return roleService.saveSubscriberToken(userRole);
    }

    @GetMapping("/token/{userId}")
    public Flux<Object> getUserRoleByUserId(@PathVariable("userId") Integer userId) {
        Flux<UserRole> userRoleFlux = roleService.getUserRoleByUserId(userId);
        Mono<User> userMono = userService.findById(userId);
        return Flux.zip(userMono, userRoleFlux.collectList(), (user, userRoles) -> new User(user.getId(), user.getUsername())); // TODO - Pass user roles
    }

    @GetMapping("/roles")
    public Flux<UserRole> getAllRoles() {
        return roleService.getAllRoles();
    }
}
