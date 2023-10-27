package ru.school.retailanalitycs_web_java.exceptions.notFoundExceptions;

public class CustomerNotFoundException extends EntityNotFoundException {
    public CustomerNotFoundException(int id) {
        super(String.format("Customer with id {%d} not found", id));
    }
}
