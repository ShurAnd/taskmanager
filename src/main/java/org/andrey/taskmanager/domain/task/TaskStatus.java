package org.andrey.taskmanager.domain.task;

import lombok.Getter;

@Getter
public enum TaskStatus {

    PENDING(0, "В ожидании"),
    IN_PROCESS(1, "В процессе"),
    FINISHED(2, "Завершено");

    private int code;
    private String value;

    TaskStatus(int code, String value) {
        this.code = code;
        this.value = value;
    }

}
