package ru.school.retailanalitycs_web_java.exceptions.notFoundExceptions;

public class StoreNotFoundException extends EntityNotFoundException {
    public StoreNotFoundException(Integer transactionStoreId) {
        super(String.format("Store with transaction store id {%s} not found", transactionStoreId));

    }
}
