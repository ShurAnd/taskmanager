package org.andrey.taskmanager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.andrey.taskmanager.domain.user.User;
import org.andrey.taskmanager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Контроллер для админа по управлению пользователями
 */
@RestController
@RequestMapping("api/admin/users")
@PreAuthorize("hasRole('ADMIN')")
public class UserController {

    private final UserService userService;
    private final ObjectMapper objectMapper;

    @Autowired
    public UserController(UserService userService,
                          ObjectMapper objectMapper) {
        this.userService = userService;
        this.objectMapper = objectMapper;
    }

    /**
     * Метод для поиска всех пользователей
     */
    @GetMapping
    public ResponseEntity<String> findAll() throws Exception {
        List<User> result = userService.getAllUsers();
        return ResponseEntity.ok()
                .header("Content-Type", "application/json")
                .body(objectMapper.writeValueAsString(result));
    }

    /**
     * Метод для поиска пользователя по Id
     */
    @GetMapping("/{id}")
    public ResponseEntity<String> findOneById(@PathVariable Long id) throws Exception {
        User result = userService.getUserById(id);
        result.setPassword("");
        return ResponseEntity.ok()
                .header("Content-Type", "application/json")
                .body(objectMapper.writeValueAsString(result));
    }

    /**
     * Метод для обновления данных пользователя
     */
    @PutMapping
    public ResponseEntity<String> updateOne(@RequestBody User user) throws Exception {
        User result = userService.updateUser(user);
        result.setPassword("");
        return ResponseEntity.ok()
                .header("Content-Type", "application/json")
                .body(objectMapper.writeValueAsString(result));
    }

    /**
     * Метод для добавления роли пользователю
     */
    @PatchMapping("/{userId}/authority/add/{authorityCode}")
    public ResponseEntity<String> addAuthorityForUser(@PathVariable Long userId,
                                                      @PathVariable Integer authorityCode) {
        userService.addAuthorityForUser(authorityCode, userId);
        return ResponseEntity.ok().build();
    }

    /**
     * Метод для удаления роли у пользователя
     */
    @PatchMapping("/{userId}/authority/delete/{authorityCode}")
    public ResponseEntity<String> deleteAuthorityForUser(@PathVariable Long userId,
                                                         @PathVariable Integer authorityCode) {
        userService.deleteAuthorityForUser(authorityCode, userId);
        return ResponseEntity.ok().build();
    }

    /**
     * Метод для удаления пользователя
     */
    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUserById(@PathVariable Long userId,
                                                 @PathVariable Integer authorityCode) {
        userService.deleteUserById(userId);
        return ResponseEntity.noContent().build();
    }
}
