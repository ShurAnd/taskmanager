package org.andrey.taskmanager.domain.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
    @NotBlank(message = "Нельзя не указывать логин пользователя")
    private String username = "";
    //    Пароль пользователя
    @NotBlank(message = "Нельзя не указывать пароль пользователя")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password = "";
}
