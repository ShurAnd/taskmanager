package org.andrey.taskmanager.service;

import jakarta.transaction.Transactional;
import org.andrey.taskmanager.domain.user.User;
import org.andrey.taskmanager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Класс сервис для выполнения операций над пользователями приложения
 */
@Service
public class UserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository repository,
                       PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user = repository.save(user);
        repository.addAuthorityForUser("USER", user.getId());
        return user;
    }

    public User updateUser(User user) {
        return repository.save(user);
    }

    public void deleteUserById(Long id) {
        repository.deleteById(id);
    }

    public void deleteAuthorityForUser(String authority, Long id) {
        repository.deleteAuthorityForUser(authority, id);
    }

    public void addAuthorityForUser(String authority, Long id) {
        repository.addAuthorityForUser(authority, id);
    }

    public List<User> getAllUsers() {
        return repository.findAll();
    }

    public User getUserById(Long id) {
//        TODO исключение если пользователь не найден
        return repository.findById(id).orElseThrow(null);
    }


}
