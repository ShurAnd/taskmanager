package org.andrey.taskmanager.domain.task;

import lombok.Getter;

import java.util.Arrays;

/**
 * Enum описывающий статус задачи
 */
@Getter
public enum TaskStatus {
    // Ожидание начала выполнения
    PENDING(0, "В ожидании"),
    // В процессе выполнения
    IN_PROCESS(1, "В процессе"),
    // Завершена
    FINISHED(2, "Завершено");

//    Код статуса задачи
    private int code;
//    Текстовое описание статуса задачи
    private String value;

    TaskStatus(int code, String value) {
        this.code = code;
        this.value = value;
    }

    /**
     * Метод для получения статуса задачи из кода
     */
    public static TaskStatus fromCode(int code){
        return Arrays.stream(values())
                .filter( c -> c.getCode() == code)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Неизвестный код статуса задачи"));
    }
}
