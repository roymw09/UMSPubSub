package org.ac.cst8277.williams.roy.repository;

import org.ac.cst8277.williams.roy.model.User;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface JwtAuthenticationRepository extends ReactiveCrudRepository<User, Integer> {
    @Query("SELECT username from users WHERE username = :username")
    Mono<String> getUsername(@Param("username") String username);

    @Query("SELECT username from users WHERE username = :username AND id = :user_id")
    Mono<String> getUsernameById(@Param("user_id") Integer user_id, @Param("username") String username);

    @Query("SELECT role FROM user_roles WHERE role_id = :token")
    Mono<String> getRoleByToken(@Param("token") String token);

    @Query("SELECT role FROM user_roles WHERE refresh_token = :refreshToken")
    Mono<String> getRoleByRefreshToken(@Param("refreshToken") String refreshToken);

    @Query("UPDATE user_roles SET role_id = :newToken WHERE role = :role")
    Mono<String> updateToken(@Param("newToken") String newToken, @Param("role") String role);

    @Query("SELECT user_roles.role FROM user_roles, users WHERE users.username = :username AND users.id = user_roles.user_id")
    Flux<String> getRoleFromUsername(@Param("username") String username);

    @Query("SELECT refresh_token FROM user_roles WHERE role_id = :jwtToken")
    Mono<String> getRefreshToken(@Param("jwtToken") String jwtToken);
}
