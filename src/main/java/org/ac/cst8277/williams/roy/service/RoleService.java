package org.ac.cst8277.williams.roy.service;

import lombok.extern.slf4j.Slf4j;
import org.ac.cst8277.williams.roy.model.UserRole;
import org.ac.cst8277.williams.roy.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@Transactional
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public Mono<UserRole> savePublisherToken(UserRole userRole) {
        return roleRepository.save(userRole);
    }

    public Mono<UserRole> saveSubscriberToken(UserRole userRole) { return roleRepository.save(userRole); }

    public Flux<UserRole> getUserRoleByUserId(Integer userId) {
        return roleRepository.getUserRoleByUserId(userId);
    }

    public Flux<UserRole> getAllRoles() { return roleRepository.findAll(); }

    public Mono<UserRole> getPublisherRoleByUserId(Integer userId) { return roleRepository.getPublisherRoleByUserId(userId); }
}
