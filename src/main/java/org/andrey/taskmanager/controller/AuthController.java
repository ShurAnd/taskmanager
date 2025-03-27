package org.andrey.taskmanager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.andrey.taskmanager.domain.user.RegisterUser;
import org.andrey.taskmanager.domain.user.User;
import org.andrey.taskmanager.domain.user.UserCreds;
import org.andrey.taskmanager.security.jwt.JWTUtils;
import org.andrey.taskmanager.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/authenticate")
public class AuthController {
    private static final String TAG = "AUTH CONTROLLER";
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthenticationProvider authenticationProvider;
    private final JWTUtils jwtUtil;
    private final UserDetailsService userDetailsService;
    private final UserService userService;
    private final ObjectMapper objectMapper;

    @Autowired
    public AuthController(AuthenticationProvider authenticationProvider,
                          JWTUtils jwtUtil,
                          UserDetailsService userDetailsService,
                          UserService userService,
                          ObjectMapper objectMapper) {
        this.authenticationProvider = authenticationProvider;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.userService = userService;
        this.objectMapper = objectMapper;
    }

    @PostMapping
    public String authenticate(@RequestBody UserCreds userCreds) throws AuthenticationException {
        try {
            authenticationProvider.authenticate(
                    new UsernamePasswordAuthenticationToken(userCreds.getEmail(), userCreds.getPassword())
            );
        } catch (AuthenticationException e) {
            logger.error("{} - Ошибка во время аутентификации", TAG);
            throw e;
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(userCreds.getEmail());
        return jwtUtil.generateToken(userDetails.getUsername());
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterUser registerUser) throws Exception {
        User user = new User();
        user.setEmail(registerUser.getEmail());
        user.setFirstName(registerUser.getFirstName());
        user.setLastName(registerUser.getLastName());
        user.setPassword(registerUser.getPassword());
        user = userService.createUser(user);

        return ResponseEntity
                .created(null)
                .header("Content-Type", "application/json")
                .body(objectMapper.writeValueAsString(user));
    }
}