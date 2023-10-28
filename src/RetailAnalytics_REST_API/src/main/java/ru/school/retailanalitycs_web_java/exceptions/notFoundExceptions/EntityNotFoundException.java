package ru.school.retailanalitycs_web_java.exceptions.notFoundExceptions;

import ru.school.retailanalitycs_web_java.exceptions.BaseExceptions;

import static ru.school.retailanalitycs_web_java.exceptions.ExceptionCode.NOT_FOUND;

public abstract class EntityNotFoundException extends BaseExceptions {
    protected EntityNotFoundException(String message) {
        super(NOT_FOUND, message);
    }
}
