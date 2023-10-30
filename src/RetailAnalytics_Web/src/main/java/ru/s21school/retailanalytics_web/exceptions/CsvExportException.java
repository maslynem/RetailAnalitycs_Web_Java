package ru.s21school.retailanalytics_web.exceptions;

public class CsvExportException extends RuntimeException {
    public CsvExportException(String message) {
        super(message);
    }
}
