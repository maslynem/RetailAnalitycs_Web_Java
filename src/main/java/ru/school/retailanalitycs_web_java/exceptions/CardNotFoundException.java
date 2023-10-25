package ru.school.retailanalitycs_web_java.exceptions;

import static ru.school.retailanalitycs_web_java.exceptions.ExceptionCode.NOT_FOUND;

public class CardNotFoundException extends EntityNotFoundException {
    public CardNotFoundException(Integer id) {
        super(NOT_FOUND, String.format("Card with id {%s} not found", id));

    }
}
