package org.andrey.taskmanager.repository;

import jakarta.transaction.Transactional;
import org.andrey.taskmanager.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Интерфейс объявляющий методы для взаимодействия User с БД
 */
@Transactional
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query(value = "SELECT * FROM users WHERE username = :username", nativeQuery = true)
    User findUserByUsername(String username);

    @Query(value = "SELECT authority FROM authorities WHERE user_id = :id", nativeQuery = true)
    List<String> findAuthoritiesByUser(Long id);

    @Modifying
    @Query(value = "INSERT INTO authorities (authority, user_id) VALUES (:authority, :id)", nativeQuery = true)
    int addAuthorityForUser(String authority, Long id);

    @Modifying
    @Query(value = "DELETE FROM authorities WHERE authority = :authority and user_id = :id", nativeQuery = true)
    int deleteAuthorityForUser(String authority, Long id);
}
