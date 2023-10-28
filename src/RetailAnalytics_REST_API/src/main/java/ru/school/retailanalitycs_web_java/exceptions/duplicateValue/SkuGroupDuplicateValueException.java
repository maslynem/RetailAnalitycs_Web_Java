package ru.school.retailanalitycs_web_java.exceptions.duplicateValue;

public class SkuGroupDuplicateValueException extends DuplicateValueException {
    public SkuGroupDuplicateValueException(String groupName, Long id) {
        super(String.format("Sku Group with group name {%s} is already exist. Id is {%d}", groupName, id));
    }
}
