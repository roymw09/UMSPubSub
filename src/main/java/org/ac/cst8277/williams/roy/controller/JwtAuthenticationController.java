package org.ac.cst8277.williams.roy.controller;

import org.ac.cst8277.williams.roy.model.UserRole;
import org.ac.cst8277.williams.roy.service.JwtAuthenticationService;
import org.ac.cst8277.williams.roy.util.JwtRequest;
import org.ac.cst8277.williams.roy.util.JwtResponse;
import org.ac.cst8277.williams.roy.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@CrossOrigin
@RequestMapping("/authenticate")
public class JwtAuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtAuthenticationService userDetailsService;

    @PostMapping("/publisher")
    public ResponseEntity<JwtResponse> createPublisherAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {

        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

        final UserDetails userDetails = userDetailsService
                .getUsernameById(authenticationRequest.getUser_id(), authenticationRequest.getUsername());

        // generate publisher token based on user data
        final String token = jwtTokenUtil.generateToken(userDetails);
        final String refreshToken = jwtTokenUtil.generateRefreshToken(userDetails);

        // save publisher token to database
        UserRole userRole = new UserRole(authenticationRequest.getUser_id(), token, "PUBLISHER", "Message content producer", refreshToken);
        HttpEntity<UserRole> httpRequest = new HttpEntity<>(userRole);
        new RestTemplate().exchange("https://hidden-tundra-10439.herokuapp.com/users/role/token/savePublisher", HttpMethod.POST, httpRequest, String.class);

        return ResponseEntity.status(HttpStatus.OK).body(new JwtResponse(token));
    }

    @PostMapping("/subscriber")
    public ResponseEntity<JwtResponse> createSubscriberAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {

        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

        final UserDetails userDetails = userDetailsService
                .getUsernameById(authenticationRequest.getUser_id(), authenticationRequest.getUsername());

        // generate subscriber token based on user data
        final String token = jwtTokenUtil.generateToken(userDetails);
        final String refreshToken = jwtTokenUtil.generateRefreshToken(userDetails);

        // save subscriber token to database
        UserRole userRole = new UserRole(authenticationRequest.getUser_id(), token, "SUBSCRIBER", "Message content consumer", refreshToken);
        HttpEntity<UserRole> httpRequest = new HttpEntity<>(userRole);
        new RestTemplate().exchange("https://hidden-tundra-10439.herokuapp.com/users/role/token/saveSubscriber", HttpMethod.POST, httpRequest, String.class);

        return ResponseEntity.ok(new JwtResponse(token));
    }

    // all requests are filtered by JwtRequestFilter
    // requests are forwarded to this endpoint which returns OK if the token is valid
    // JwtRequestFilter class sets the response status to 401 unauthorized if token is NOT valid
    @GetMapping("/validate")
    public ResponseEntity<String> validateToken() {
        return ResponseEntity.ok("VALID");
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
}
