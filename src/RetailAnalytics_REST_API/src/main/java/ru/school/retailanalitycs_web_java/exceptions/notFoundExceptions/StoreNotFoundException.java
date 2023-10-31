package ru.school.retailanalitycs_web_java.exceptions.notFoundExceptions;

import ru.school.retailanalitycs_web_java.entities.tables.StoreId;

public class StoreNotFoundException extends EntityNotFoundException {
    public StoreNotFoundException(StoreId storeId) {
        super(String.format("Store with transaction store id {%s} and sku id {%d} not found",
                storeId.getTransactionStoreId(), storeId.getSku().getId()));
    }

    public StoreNotFoundException(Long transactionStoreId) {
        super(String.format("Store with transaction store id {%d} not found",
                transactionStoreId));
    }
}
