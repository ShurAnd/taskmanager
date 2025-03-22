package org.andrey.taskmanager.domain.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Класс для описания пользователя Системы Управления Задачами
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
//    Идентификатор пользователя
    private Long id = -1L;
//    Имя пользователя
    private String firstName = "";
//    Фамилия пользователя
    private String lastName = "";
//    Логин пользователя
    private String username = "";
//    Пароль пользователя
    private String password = "";
}
