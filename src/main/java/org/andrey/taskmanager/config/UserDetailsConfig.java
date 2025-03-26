package org.andrey.taskmanager.config;

import org.andrey.taskmanager.repository.UserRepository;
import org.andrey.taskmanager.security.JdbcUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Класс конфигурации для создания бинов UserDetailsService и PasswordEncoder, участвующих
 * в обработке пользовательских логинов и паролей
 */
@Configuration
@EnableTransactionManagement
public class UserDetailsConfig {

    private final UserRepository userRepository;

    @Value("${admin.username:admin}")
    private String defaultAdminUsername;
    @Value("${admin.password:admin}")
    private String defaultAdminPassword;

    @Autowired
    public UserDetailsConfig(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        JdbcUserDetailsService userDetailsService = new JdbcUserDetailsService(userRepository, passwordEncoder());
        userDetailsService.createDefaultAdmin(defaultAdminUsername, defaultAdminPassword);


        return userDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(7);
    }
}
