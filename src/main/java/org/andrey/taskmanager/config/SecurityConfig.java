package org.andrey.taskmanager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Класс конфигурации отвечающий за объекты управляющие аутентификацией и авторизацией
 */
@Configuration
public class SecurityConfig {

    @Bean
    public UserDetailsService userDetailsService(){
        return null;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(7);
    }
}
