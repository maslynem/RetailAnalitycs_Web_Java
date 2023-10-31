package ru.school.retailanalitycs_web_java.exceptions;

import static ru.school.retailanalitycs_web_java.exceptions.ExceptionCode.SERVER_ERROR;

public class LoadSqlFileException extends BaseExceptions {
    public LoadSqlFileException(String message) {
        super(SERVER_ERROR, message);
    }
}
