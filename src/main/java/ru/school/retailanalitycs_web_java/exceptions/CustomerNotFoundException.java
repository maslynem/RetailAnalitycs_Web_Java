package ru.school.retailanalitycs_web_java.exceptions;

import static ru.school.retailanalitycs_web_java.exceptions.ExceptionCode.NOT_FOUND;

public class CustomerNotFoundException extends EntityNotFoundException {
    public CustomerNotFoundException(int id) {
        super(NOT_FOUND, String.format("Customer with id {%s} not found", id));
    }
}
