package com.springcloud.msvc.users;

/*
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;
*/

/**
 * This RESOURCE SERVER will enable us to authorize the incoming user which uses a jwt token
 * who was already authenticated with a 'ROLE' and needs to get access to some resource
 * This will communicate with the authentication server to validate the token and allow the user to consume
 * the desired resource of this microservice.
 */

//@EnableWebSecurity
public class SecurityConfig {

    /*
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{

        //all the routes required authentication
        http.authorizeHttpRequests((authorize) -> authorize
                        // we allow to get the jwt token based on the authorization code to everybody
                        .requestMatchers("/authorized").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/users", "/api/users/{id}").hasAnyAuthority("SCOPE_read", "SCOPE_write")
                        .requestMatchers(HttpMethod.POST, "/api/users").hasAuthority("SCOPE_write")
                        .requestMatchers(HttpMethod.PUT, "/api/users/{id}").hasAuthority("SCOPE_write")
                        .requestMatchers(HttpMethod.DELETE, "/api/users/{id}").hasAuthority("SCOPE_write")
                        .anyRequest().authenticated()
                )
                // we disable the session management to not save the state in the authentication
                .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // we enable the authentication page for our client
                .oauth2Login(oauth2Login -> oauth2Login.loginPage("/oauth2/authorization/msvc-users-client"))
                .oauth2Client(withDefaults())
                //authorize the user with a incoming token who has the right permissions to access the resource
                // this user has a ROLE with scopes of 'read' or 'write'
                .oauth2ResourceServer((oauth2) -> oauth2.jwt(withDefaults()));
                // if we need to set the uri of the resource server to validate the token we can use as follows
                /*.oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                                .jwkSetUri(serviceUrl)
                        )
                );
                //.oauth2ResourceServer().jwt();
        return http.build();
    }*/
}
