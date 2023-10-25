package ru.school.retailanalitycs_web_java.exceptions.notFoundExceptions;

public class CardNotFoundException extends EntityNotFoundException {
    public CardNotFoundException(Integer id) {
        super(String.format("Card with id {%s} not found", id));

    }
}
