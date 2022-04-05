package org.ac.cst8277.williams.roy.repository;

import org.ac.cst8277.williams.roy.model.User;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveCrudRepository<User, Integer> {
    @Query("select * from users where id = :userId")
    Mono<User> findById(@Param("userId") Integer userId);

    @Query("UPDATE users SET username = :username, roles = array_append(roles, :userRole) WHERE id = :userId")
    Mono<User> updateUser(@Param("username") String username,
                          @Param("userRole") String userRole,
                          @Param("userId") Integer userId);

    @Query("SELECT username FROM users WHERE id = :userId")
    Mono<String> getUsernameById(@Param("userId") Integer userId);
}