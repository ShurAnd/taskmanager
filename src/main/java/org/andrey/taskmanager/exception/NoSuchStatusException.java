package org.andrey.taskmanager.exception;

import lombok.Getter;

@Getter
public class NoSuchStatusException extends RuntimeException {
    public NoSuchStatusException(String message) {
        super(message);
    }
}
