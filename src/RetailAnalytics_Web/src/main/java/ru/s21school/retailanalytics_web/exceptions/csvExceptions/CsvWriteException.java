package ru.s21school.retailanalytics_web.exceptions.csvExceptions;

public class CsvWriteException extends RuntimeException {
    public CsvWriteException(String message) {
        super(message);
    }
}
