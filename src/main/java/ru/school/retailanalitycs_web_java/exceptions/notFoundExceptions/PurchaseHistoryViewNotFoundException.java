package ru.school.retailanalitycs_web_java.exceptions.notFoundExceptions;

import ru.school.retailanalitycs_web_java.entities.views.purchaseHistoryView.PurchaseHistoryViewId;

public class PurchaseHistoryViewNotFoundException extends EntityNotFoundException {
    public PurchaseHistoryViewNotFoundException(PurchaseHistoryViewId id) {
        super(String.format("Purchase History with customer id {%d} and transaction id {%d} not found", id.getCustomerId(), id.getTransactionId()));
    }
}
