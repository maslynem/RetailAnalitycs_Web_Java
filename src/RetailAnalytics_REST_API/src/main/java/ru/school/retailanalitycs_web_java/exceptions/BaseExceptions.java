package ru.school.retailanalitycs_web_java.exceptions;

import lombok.Getter;

@Getter
public class BaseExceptions extends RuntimeException {
    private final ExceptionCode code;

    public BaseExceptions(ExceptionCode code, String message) {
        super(message);
        this.code = code;
    }
}
