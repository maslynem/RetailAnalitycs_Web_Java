package ru.school.retailanalitycs_web_java.exceptions;

public abstract class EntityNotFoundException extends BaseExceptions {
    protected EntityNotFoundException(ExceptionCode code, String message) {
        super(code, message);
    }
}
