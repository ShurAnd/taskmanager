package org.andrey.taskmanager.domain.task;

import lombok.Getter;

@Getter
public enum TaskPriority {

    LOW(0, "Низкий приоритет"),
    MEDIUM(1, "Средний приоритет"),
    HIGH(2, "Высокий приоритет");

    private int code;
    private String value;

    TaskPriority(int code, String value) {
        this.code = code;
        this.value = value;
    }

}
