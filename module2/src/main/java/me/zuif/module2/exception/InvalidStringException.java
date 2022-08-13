package me.zuif.module2.exception;

import java.io.IOException;

public class InvalidStringException extends IOException {

    public InvalidStringException(String message) {
        super(message);
    }

    public InvalidStringException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidStringException(Throwable cause) {
        super(cause);
    }
}
