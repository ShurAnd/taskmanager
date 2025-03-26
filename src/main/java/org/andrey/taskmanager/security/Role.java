package org.andrey.taskmanager.security;

import lombok.Getter;

import java.util.Arrays;

/**
 * Enum для описания ролей пользователя
 */
@Getter
public enum Role {
    // Роль администратора
    ADMIN(0, "Администратор"),
    // Роль пользователя
    USER(1, "Обычный пользователь");

    //    Код роли
    private int code;
    //    Текстовое описание роли
    private String value;

    Role(int code, String value) {
        this.code = code;
        this.value = value;
    }

    /**
     * Метод для получения роли из кода
     */
    public static Role fromCode(int code) {
        return Arrays.stream(values())
                .filter(c -> c.getCode() == code)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Неизвестный код роли"));
    }
}
