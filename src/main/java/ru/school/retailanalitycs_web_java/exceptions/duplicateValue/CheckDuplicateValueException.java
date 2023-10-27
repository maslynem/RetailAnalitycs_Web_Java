package ru.school.retailanalitycs_web_java.exceptions.duplicateValue;

import ru.school.retailanalitycs_web_java.entities.tables.CheckId;

public class CheckDuplicateValueException extends DuplicateValueException {
    public CheckDuplicateValueException(CheckId id) {
        super(String.format("Check with transaction id {%d} and sku id {%d} is already exist.", id.getTransactionId(), id.getSkuId()));
    }
}
