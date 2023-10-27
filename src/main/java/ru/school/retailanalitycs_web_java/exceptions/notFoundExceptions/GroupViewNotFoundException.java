package ru.school.retailanalitycs_web_java.exceptions.notFoundExceptions;

import ru.school.retailanalitycs_web_java.entities.views.groupView.GroupViewId;

public class GroupViewNotFoundException extends EntityNotFoundException {
    public GroupViewNotFoundException(GroupViewId id) {
        super(String.format("Group View with customer id {%d} and sku group id {%d} not found", id.getCustomerId(), id.getSkuGroupId()));
    }
}
