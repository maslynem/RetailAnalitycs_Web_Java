package ru.school.retailanalitycs_web_java.exceptions.notFoundExceptions;

public class SkuGroupNotFoundException extends EntityNotFoundException {
    public SkuGroupNotFoundException(Integer id) {
        super(String.format("Sku Group with id {%s} not found", id));
    }
}
