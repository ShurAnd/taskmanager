package org.andrey.taskmanager.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TaskNotFoundException extends RuntimeException {
    private String message = "";
}
