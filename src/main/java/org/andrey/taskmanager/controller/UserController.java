package org.andrey.taskmanager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.andrey.taskmanager.domain.user.User;
import org.andrey.taskmanager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final ObjectMapper objectMapper;

    @Autowired
    public UserController(UserService userService,
                          ObjectMapper objectMapper) {
        this.userService = userService;
        this.objectMapper = objectMapper;
    }

    @GetMapping
    public ResponseEntity<String> findAll() throws Exception{
        List<User> result = userService.getAllUsers();
        return ResponseEntity.ok()
                .header("Content-Type", "application/json")
                .body(objectMapper.writeValueAsString(result));
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> findOneById(@PathVariable Long id) throws Exception{
        User result = userService.getUserById(id);
        result.setPassword("");
        return ResponseEntity.ok()
                .header("Content-Type", "application/json")
                .body(objectMapper.writeValueAsString(result));
    }
}
