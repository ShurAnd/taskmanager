package org.andrey.taskmanager.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class JWTTokenException extends RuntimeException {
    private String message;
}
