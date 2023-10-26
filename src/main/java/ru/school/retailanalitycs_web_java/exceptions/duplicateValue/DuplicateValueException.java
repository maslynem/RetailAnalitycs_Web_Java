package ru.school.retailanalitycs_web_java.exceptions.duplicateValue;

import ru.school.retailanalitycs_web_java.exceptions.BaseExceptions;

import static ru.school.retailanalitycs_web_java.exceptions.ExceptionCode.DUPLICATE_VALUE;

public abstract class DuplicateValueException extends BaseExceptions {
    protected DuplicateValueException(String message) {
        super(DUPLICATE_VALUE, message);
    }
}
