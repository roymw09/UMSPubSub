package org.ac.cst8277.williams.roy.service;

import lombok.extern.slf4j.Slf4j;
import org.ac.cst8277.williams.roy.model.UserRole;
import org.ac.cst8277.williams.roy.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@Transactional
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public Mono<UserRole> saveUserId(Integer userId) {
        return roleRepository.saveUserId(userId);
    }

    public Mono<UserRole> savePublisherToken(String publisherToken, Integer userId) {
        return roleRepository.savePublisherToken(publisherToken, userId);
    }

    public Mono<UserRole> saveSubscriberToken(String subscriberToken, Integer userId) {
        return roleRepository.saveSubscriberToken(subscriberToken, userId);
    }

    public Mono<UserRole> getUserRoleByUserId(Integer userId) {
        return roleRepository.getUserRoleByUserId(userId);
    }
}
