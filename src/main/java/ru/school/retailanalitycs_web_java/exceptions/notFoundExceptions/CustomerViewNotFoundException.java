package ru.school.retailanalitycs_web_java.exceptions.notFoundExceptions;

public class CustomerViewNotFoundException extends EntityNotFoundException {
    public CustomerViewNotFoundException(int id) {
        super(String.format("Customer View with id {%d} not found", id));
    }
}
