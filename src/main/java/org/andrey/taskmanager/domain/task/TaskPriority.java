package org.andrey.taskmanager.domain.task;

import lombok.Getter;
import org.andrey.taskmanager.exception.NoSuchPriorityException;

import java.util.Arrays;

/**
 * Enum описывающий статус задачи
 */
@Getter
public enum TaskPriority {

    LOW(0, "Низкий приоритет"),
    MEDIUM(1, "Средний приоритет"),
    HIGH(2, "Высокий приоритет");

    //    Код приоритета задачи
    private int code;
    //    Текстовое описание приоритета задачи
    private String value;

    TaskPriority(int code, String value) {
        this.code = code;
        this.value = value;
    }

    /**
     * Метод для получения приоритета задачи из кода
     */
    public static TaskPriority fromCode(int code) {
        return Arrays.stream(values())
                .filter(c -> c.getCode() == code)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Неизвестный код приоритета задачи"));
    }

    /**
     * Метод для получения приоритета задачи из строкового значения
     */
    public static TaskPriority fromValue(String value) {
        try {
            return valueOf(value.toUpperCase());
        } catch (Exception ex) {
            throw new NoSuchPriorityException("Неверное значение для приоритета задачи");
        }
    }
}
