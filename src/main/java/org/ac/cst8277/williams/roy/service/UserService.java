package org.ac.cst8277.williams.roy.service;

import org.ac.cst8277.williams.roy.model.User;
import org.ac.cst8277.williams.roy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import java.util.List;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Mono<User> createUser(User user) {
        return userRepository.save(user);
    }

    public Flux<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Mono<User> findById(Integer userId) {
        return userRepository.findById(userId);
    }

    public Mono<User> updateUser(Integer userId, User user) {
        return userRepository.updateUser(user.getUsername(), "1", userId);
    }

    public Mono<User> deleteUser(Integer userId) {
        return userRepository.findById(userId)
                .flatMap(existingUser -> userRepository.delete(existingUser)
                        .then(Mono.just(existingUser)));
    }

    public Flux<User> fetchUsers(List<Integer> userIds) {
        return Flux.fromIterable(userIds)
                .parallel()
                .runOn(Schedulers.boundedElastic())
                .flatMap(this::findById)
                .ordered((u1, u2) -> u2.getId() - u1.getId());
    }

    public Mono<String> getUsernameById(Integer userId) {
        return userRepository.getUsernameById(userId);
    }

    public Mono<User> checkIfUserExists(String username) {
        return userRepository.checkIfUserExists(username);
    }
}