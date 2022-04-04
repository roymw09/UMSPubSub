package org.ac.cst8277.williams.roy.config;

import org.ac.cst8277.williams.roy.filter.JwtRequestFilter;
import org.ac.cst8277.williams.roy.util.JwtAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class MultiHttpSecurityConfig {

    @Configuration
    @Order(2)
    public static class AuthSecurityConfig extends WebSecurityConfigurerAdapter {

        @Autowired
        private AuthenticationFailureHandler handler;

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .authorizeRequests(a -> a
                            .antMatchers("/", "/users/user/error", "/webjars/**").permitAll()
                            .anyRequest().authenticated()
                    )
                    .logout(l -> l
                            .logoutSuccessUrl("/").permitAll()
                    )
                    .csrf(c -> c
                            .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                    )
                    .exceptionHandling(e -> e
                            .authenticationEntryPoint(new
                                    HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                    )
                    .oauth2Login(o -> o
                            .failureHandler((request, response, exception) -> {
                                request.getSession().setAttribute("error.message", exception.getMessage());
                                handler.onAuthenticationFailure(request, response, exception);
                            }));
        }
    }

    @Configuration
    @Order(1)
    public static class JWTSecurityConfig extends WebSecurityConfigurerAdapter {

        @Autowired
        private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

        @Autowired
        private UserDetailsService jwtUserDetailsService;

        @Autowired
        private JwtRequestFilter jwtRequestFilter;

        @Autowired
        public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
            // configure AuthenticationManager so that it knows from where to load
            // user for matching credentials
            // Use BCryptPasswordEncoder
            auth.userDetailsService(jwtUserDetailsService).passwordEncoder(passwordEncoder());
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }

        @Bean
        @Override
        public AuthenticationManager authenticationManagerBean() throws Exception {
            return super.authenticationManagerBean();
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.csrf().disable()
                    .requestMatchers()
                    .antMatchers("/authenticate")
                    .and()
                    .authorizeRequests(a -> a
                            // dont authenticate this particular request
                            .antMatchers("/authenticate").permitAll()
                            .anyRequest().authenticated()
                    )
                    // all other requests need to be authenticated
                    // make sure we use stateless session; session won't be used to
                    // store user's state.
                    .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint).and().sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

            // Add a filter to validate the tokens with every request
            http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        }
    }
}
