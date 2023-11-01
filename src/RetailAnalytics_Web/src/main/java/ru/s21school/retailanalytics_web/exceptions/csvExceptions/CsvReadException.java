package ru.s21school.retailanalytics_web.exceptions.csvExceptions;

public class CsvReadException extends RuntimeException {
    public CsvReadException(String message) {
        super(message);
    }
}
