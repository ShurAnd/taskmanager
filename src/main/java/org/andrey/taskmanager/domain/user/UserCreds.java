package org.andrey.taskmanager.domain.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Класс для обработки пользовательского логина и пароля
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCreds {
    private String email;
    private String password;
}
