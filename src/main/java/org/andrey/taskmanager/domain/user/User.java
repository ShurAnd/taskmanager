package org.andrey.taskmanager.domain.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Класс для описания пользователя Системы Управления Задачами
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "users")
public class User {
    //    Идентификатор пользователя
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id = -1L;
    //    Имя пользователя
    @NotBlank(message = "Нельзя не указывать имя пользователя")
    private String firstName = "";
    //    Фамилия пользователя
    @NotBlank(message = "Нельзя не указывать фамилию пользователя")
    private String lastName = "";
    //    Логин пользователя
    @Email(message = "Неверно введен Email пользователя", regexp = "([a-zA-Z0-9._-]+@[a-zA-Z0-9._-]+\\.[a-zA-Z0-9_-]+)")
    @NotBlank(message = "Нельзя не указывать логин пользователя")
    @Column(unique = true, name = "username")
    private String email = "";
    //    Пароль пользователя
    @NotBlank(message = "Нельзя не указывать пароль пользователя")
    private String password = "";
}
