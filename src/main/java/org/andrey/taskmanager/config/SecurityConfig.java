package org.andrey.taskmanager.config;

import org.andrey.taskmanager.repository.UserRepository;
import org.andrey.taskmanager.security.jwt.JWTRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Класс конфигурации отвечающий за объекты управляющие аутентификацией и авторизацией
 */
@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final UserRepository userRepository;
    private final AuthenticationProvider authenticationProvider;
    private final JWTRequestFilter jwtRequestFilter;

    @Autowired
    public SecurityConfig(UserRepository userRepository,
                          AuthenticationProvider authenticationProvider,
                          JWTRequestFilter jwtRequestFilter) {
        this.userRepository = userRepository;
        this.authenticationProvider = authenticationProvider;
        this.jwtRequestFilter = jwtRequestFilter;
    }

    @Bean
    public SecurityFilterChain httpSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
                .authenticationProvider(authenticationProvider)
                .authorizeHttpRequests(c ->
                        c.requestMatchers(HttpMethod.POST, "/authenticate/**").permitAll()
                                .requestMatchers("/error").permitAll()
                                .requestMatchers("/swagger-ui/**").permitAll()
                                .requestMatchers("/openapi.yaml").permitAll()
                                .requestMatchers("/v3/**").permitAll()
                                .requestMatchers("/users/**").permitAll()
                                .anyRequest().authenticated());

        return http.build();
    }
}
