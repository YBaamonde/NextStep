package com.nextstep.nextstepBackEnd.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests(authorize -> {
                    try {
                        authorize
                                .requestMatchers("/login").permitAll() // allow unauthenticated access to /login
                                .requestMatchers("/auth/register").permitAll() // allow unauthenticated access to /auth/register
                                .requestMatchers("/css/**", "/js/**", "/images/**", "/error/**").permitAll() // allow access to static resources and error pages
                                .anyRequest().authenticated(); // all other requests require authentication
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .formLogin(formLogin -> {
                    try {
                        formLogin
                                .loginPage("/login") // specify the login page
                                .defaultSuccessUrl("/home", true) // redirect to /home after successful login
                                .permitAll(); // allow unauthenticated access to the login process
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .logout(logout -> {
                    try {
                        logout
                                .logoutUrl("/logout")
                                .logoutSuccessUrl("/login?logout")
                                .permitAll();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
