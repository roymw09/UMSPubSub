package org.ac.cst8277.williams.roy.repository;

import org.ac.cst8277.williams.roy.model.UserRole;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface RoleRepository extends ReactiveCrudRepository<UserRole, Integer> {
    @Query("INSERT INTO UserRoles (user_id) VALUES (:userId)")
    Mono<UserRole> saveUserId(@Param("userId") Integer userId);

    @Query("INSERT INTO UserRoles (publisher_token) VALUES (:publisherToken)")
    Mono<UserRole> savePublisherToken(@Param("publisherToken") String publisherToken);

    @Query("INSERT INTO UserRoles (subscriber_token) VALUES (:subscriberToken)")
    Mono<UserRole> saveSubscriberToken(@Param("subscriberToken") String subscriberToken);

    @Query("SELECT * FROM UserRoles WHERE user_id = :userId")
    Mono<UserRole> getUserRoleByUserId(@Param("userId") Integer userId);
}
