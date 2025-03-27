package org.andrey.taskmanager.service;

import jakarta.transaction.Transactional;
import org.andrey.taskmanager.domain.user.User;
import org.andrey.taskmanager.exception.UserNotFoundException;
import org.andrey.taskmanager.repository.UserRepository;
import org.andrey.taskmanager.security.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Класс сервис для выполнения операций над пользователями приложения
 */
@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository repository,
                       PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public User createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user = repository.save(user);
        repository.addAuthorityForUser(Role.USER.getCode(), user.getId());
        user.setPassword("");

        return user;
    }

    public User updateUser(User user) {
        user = repository.save(user);
        user.setPassword("");

        return user;
    }

    public void deleteUserById(Long id) {
        repository.deleteById(id);
    }

    public void deleteAuthorityForUser(int authority, Long id) {
        repository.deleteAuthorityForUser(authority, id);
    }

    public void addAuthorityForUser(int authority, Long id) {
        repository.addAuthorityForUser(authority, id);
    }

    public List<User> getAllUsers() {
        List<User> result =  repository.findAll();
        result.forEach( u -> u.setPassword(""));

        return result;
    }

    public User getUserById(Long id) {
        return repository.findById(id).orElseThrow(() -> new UserNotFoundException("Пользователь с идентификатором " + id + " не найден!"));
    }


}
