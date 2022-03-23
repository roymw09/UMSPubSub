package org.ac.cst8277.williams.roy.controller;

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
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    ReactiveRedisOperations<String, String> tokenTemplate;

    @PostConstruct
    private void init() {
        this.tokenTemplate
                .listenTo(ChannelTopic.of("publisher_token"))
                .map(ReactiveSubscription.Message::getMessage).subscribe(pub_token -> {
                    //createMessage(content).subscribe();
                });
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<User> create(@RequestBody User user) {
        user.setToken(UUID.randomUUID());
        Mono<User> savedUser = userService.createUser(user);
        return savedUser.publishOn(Schedulers.boundedElastic()).map(user1 -> {
            saveUserId(user1.getId()).subscribe();
            return user1;
        });
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

    @GetMapping("/{email}/{token}")
    public Mono<ResponseEntity<User>> checkToken(@PathVariable("email") String email, @PathVariable("token") String token) {
        Mono<User> user = userService.checkUserToken(email, token);
        return user.map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping("/token/saveUser")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<UserRole> saveUserId(@RequestBody Integer userId) {
        return roleService.saveUserId(userId);
    }

    @GetMapping("/token/{userId}")
    public Mono<ResponseEntity<UserRole>> getUserRoleByUserId(@PathVariable("userId") Integer userId) {
        Mono<UserRole> userRoles = roleService.getUserRoleByUserId(userId);
        return userRoles.map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}