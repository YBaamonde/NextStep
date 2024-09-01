package com.nextstep.nextstepBackEnd;

import com.nextstep.nextstepBackEnd.config.AppConfig;
import com.nextstep.nextstepBackEnd.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class AppConfigTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    public void testBeansConfiguration() {
        AuthenticationManager authenticationManager = applicationContext.getBean(AuthenticationManager.class);
        AuthenticationProvider authenticationProvider = applicationContext.getBean(AuthenticationProvider.class);
        PasswordEncoder passwordEncoder = applicationContext.getBean(PasswordEncoder.class);
        UserDetailsService userDetailsService = applicationContext.getBean(UserDetailsService.class);

        assertNotNull(authenticationManager, "AuthenticationManager should not be null");
        assertNotNull(authenticationProvider, "AuthenticationProvider should not be null");
        assertNotNull(passwordEncoder, "PasswordEncoder should not be null");
        assertNotNull(userDetailsService, "UserDetailsService should not be null");
    }
}

