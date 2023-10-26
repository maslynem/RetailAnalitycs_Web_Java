package ru.school.retailanalitycs_web_java.exceptions.notFoundExceptions;

public class SkuNotFoundException extends EntityNotFoundException {
    public SkuNotFoundException(Integer id) {
        super(String.format("Sku with id {%s} not found", id));
    }
}
