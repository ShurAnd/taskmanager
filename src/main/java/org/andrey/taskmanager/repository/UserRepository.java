package org.andrey.taskmanager.repository;

import org.andrey.taskmanager.domain.user.User;

import java.util.List;

/**
 * Интерфейс объявляющий методы для взаимодействия User с БД
 */
public interface UserRepository {
    List<User> findAllUsers(int offset, int limit);
    User findUserById(Long id);
    User createUser(User user);
    User updateUser(User user);
    void deleteUserById(Long id);
}
