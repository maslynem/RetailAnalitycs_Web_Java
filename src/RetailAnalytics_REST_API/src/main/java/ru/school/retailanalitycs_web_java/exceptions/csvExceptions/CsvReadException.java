package ru.school.retailanalitycs_web_java.exceptions.csvExceptions;

import ru.school.retailanalitycs_web_java.exceptions.BaseExceptions;

import static ru.school.retailanalitycs_web_java.exceptions.ExceptionCode.CSV_IS_NOT_VALID;

public class CsvReadException extends BaseExceptions {
    public CsvReadException(String message) {
        super(CSV_IS_NOT_VALID, message);
    }
}
