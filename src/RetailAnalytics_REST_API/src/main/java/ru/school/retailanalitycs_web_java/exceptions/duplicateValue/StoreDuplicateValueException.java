package ru.school.retailanalitycs_web_java.exceptions.duplicateValue;

import ru.school.retailanalitycs_web_java.entities.tables.StoreId;

public class StoreDuplicateValueException extends DuplicateValueException {
    public StoreDuplicateValueException(StoreId id) {
        super(String.format("Store with transaction store id {%d} and sku id {%d} is already exist.", id.getTransactionStoreId(), id.getSku().getId()));
    }
}
