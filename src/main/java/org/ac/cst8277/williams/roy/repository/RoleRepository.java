package org.ac.cst8277.williams.roy.repository;

import org.ac.cst8277.williams.roy.model.UserRole;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RoleRepository extends ReactiveCrudRepository<UserRole, Integer> {
    @Query("SELECT * FROM user_roles WHERE user_id = :userId")
    Flux<UserRole> getUserRoleByUserId(@Param("userId") Integer userId);

    @Query("SELECT * FROM user_roles WHERE user_id = :userId AND role = 'PUBLISHER'")
    Mono<UserRole> getPublisherRoleByUserId(@Param("userId") Integer userId);
}
