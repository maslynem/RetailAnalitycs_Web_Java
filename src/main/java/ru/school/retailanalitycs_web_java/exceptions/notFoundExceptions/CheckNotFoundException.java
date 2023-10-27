package ru.school.retailanalitycs_web_java.exceptions.notFoundExceptions;

import ru.school.retailanalitycs_web_java.entities.tables.CheckId;

public class CheckNotFoundException extends EntityNotFoundException {
    public CheckNotFoundException(CheckId id) {
        super(String.format("Check with transaction id {%d} and sku id {%d} not found", id.getTransactionId(), id.getSkuId()));
    }
}
