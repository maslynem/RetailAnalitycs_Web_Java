package ru.school.retailanalitycs_web_java.exceptions.notFoundExceptions;

import ru.school.retailanalitycs_web_java.entities.views.periodView.PeriodViewId;

public class PeriodViewNotFoundException extends EntityNotFoundException {
    public PeriodViewNotFoundException(PeriodViewId id) {
        super(String.format("Period View with customer id {%d} and sku group id {%d} not found", id.getCustomerId(), id.getSkuGroupId()));
    }
}
