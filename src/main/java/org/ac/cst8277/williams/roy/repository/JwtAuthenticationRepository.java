package org.ac.cst8277.williams.roy.repository;

import org.ac.cst8277.williams.roy.model.User;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface JwtAuthenticationRepository extends ReactiveCrudRepository<User, Integer> {
    @Query("SELECT username from users WHERE username = :username")
    Mono<String> getUsername(@Param("username") String username);
}
