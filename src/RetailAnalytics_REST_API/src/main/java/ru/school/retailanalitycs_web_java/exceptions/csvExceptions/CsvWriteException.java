package ru.school.retailanalitycs_web_java.exceptions.csvExceptions;

import ru.school.retailanalitycs_web_java.exceptions.BaseExceptions;

import static ru.school.retailanalitycs_web_java.exceptions.ExceptionCode.UNKNOWN;

public class CsvWriteException extends BaseExceptions {
    public CsvWriteException(String message) {
        super(UNKNOWN, message);
    }
}
