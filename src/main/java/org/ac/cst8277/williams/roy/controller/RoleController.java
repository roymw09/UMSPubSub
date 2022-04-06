package org.ac.cst8277.williams.roy.controller;

import org.ac.cst8277.williams.roy.model.User;
import org.ac.cst8277.williams.roy.model.UserRole;
import org.ac.cst8277.williams.roy.service.RoleService;
import org.ac.cst8277.williams.roy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/users/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserService userService;


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
        return Flux.zip(userMono, userRoleFlux.collectList(), (user, userRoles) -> new User(user.getId(), user.getUsername(), userRoles));
    }

    @GetMapping("/roles")
    public Flux<UserRole> getAllRoles() {
        return roleService.getAllRoles();
    }
}
