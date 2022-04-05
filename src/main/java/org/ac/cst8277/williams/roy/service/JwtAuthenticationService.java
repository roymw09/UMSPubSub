package org.ac.cst8277.williams.roy.service;

import org.ac.cst8277.williams.roy.repository.JwtAuthenticationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.ArrayList;

@Service
public class JwtAuthenticationService implements UserDetailsService {

    @Autowired
    private JwtAuthenticationRepository jwtAuthenticationRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // check to see if username is currently stored in the database
        String usernameDTO = jwtAuthenticationRepository.getUsername(username).block();
        System.out.println(usernameDTO);
        if (usernameDTO != null) {
            return new User(username, "$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6",
                    new ArrayList<>());
        } else {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
    }
}