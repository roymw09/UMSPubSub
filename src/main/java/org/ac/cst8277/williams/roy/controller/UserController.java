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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import javax.annotation.PostConstruct;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private ReactiveRedisOperations<String, Publisher> publisherTokenTemplate;

    @Autowired
    private ReactiveRedisOperations<String, Subscriber> subscriberTokenTemplate;

    @PostConstruct
    private void initPublisherTokenReceiver() {
        this.publisherTokenTemplate
                .listenTo(ChannelTopic.of("publisher_token"))
                .map(ReactiveSubscription.Message::getMessage).subscribe(publisher -> {
                    UserRole userRole = new UserRole(publisher.getUser_id(), publisher.getId(), "PUBLISHER", "Message content producer");
                    savePublisherToken(userRole).subscribe();
                });
    }

    @PostConstruct
    private void initSubscriberTokenReceiver() {
        this.subscriberTokenTemplate
                .listenTo(ChannelTopic.of("subscriber_token"))
                .map(ReactiveSubscription.Message::getMessage).subscribe(subscriber -> {
                    UserRole userRole = new UserRole(subscriber.getUser_id(), subscriber.getId(), "SUBSCRIBER", "Message content consumer");
                    saveSubscriberToken(userRole).subscribe();
                });
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<User> create(@RequestBody User user) {
        return userService.createUser(user);
    }

    @GetMapping
    public Flux<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{userId}")
    public Mono<ResponseEntity<User>> getUserById(@PathVariable Integer userId) {
        Mono<User> user = userService.findById(userId);
        return user.map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping("/{userId}")
    public Mono<ResponseEntity<User>> updateUserById(@PathVariable Integer userId, @RequestBody User user) {
        return userService.updateUser(userId,user)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }

    @DeleteMapping("/{userId}")
    public Mono<ResponseEntity<Void>> deleteUserById(@PathVariable Integer userId) {
        return userService.deleteUser(userId)
                .map( r -> ResponseEntity.ok().<Void>build())
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping("/search/id")
    public Flux<User> fetchUsersById(@RequestBody List<Integer> ids) {
        return userService.fetchUsers(ids);
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
    public Flux<UserRole> getUserRoleByUserId(@PathVariable("userId") Integer userId) {
        return roleService.getUserRoleByUserId(userId);
    }
}