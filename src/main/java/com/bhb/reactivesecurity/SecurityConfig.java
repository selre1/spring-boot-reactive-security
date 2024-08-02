package com.bhb.reactivesecurity;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

import javax.annotation.PostConstruct;
import java.util.Arrays;

@Configuration
public class SecurityConfig {

    @Bean
    public ReactiveUserDetailsService reactiveUserDetailsService(UserRepository userRepository){
        userRepository.deleteAll();
        return username -> userRepository.findByName(username)
                .map(user -> User.withDefaultPasswordEncoder()
                        .username(user.getName())
                        .password(user.getPassword())
                        .authorities(user.getRoles().toArray(new String[0]))
                        .build());
    }

    @Bean
    CommandLineRunner userRunner(MongoOperations operations){
        return args -> {
            operations.save(new com.bhb.reactivesecurity.User("qqqqq","pwd", Arrays.asList("ROLE_USER")));
            operations.save(new com.bhb.reactivesecurity.User("manager","pwd",Arrays.asList("ROLE_USER","ROLE_ROOT")));
        };
    }

    @Bean
    SecurityWebFilterChain customSecurityPolicy(ServerHttpSecurity security){
        return security.authorizeExchange(exchage -> exchage
                        .pathMatchers(HttpMethod.POST,"/").hasRole("USER")
                        .pathMatchers(HttpMethod.DELETE,"/**").hasRole("ROOT")
                .anyExchange().authenticated()
                .and()
                .httpBasic()
                .and()
                .formLogin())
                .csrf().disable()
                .build();
    }
}
