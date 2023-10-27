package ru.school.retailanalitycs_web_java.exceptions.notFoundExceptions;

public class TransactionNotFoundException extends EntityNotFoundException {
    public TransactionNotFoundException(Long id) {
        super(String.format("Transaction with id {%s} not found", id));
    }
}
