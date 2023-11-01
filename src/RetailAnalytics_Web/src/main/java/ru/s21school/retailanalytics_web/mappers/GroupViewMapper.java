package ru.s21school.retailanalytics_web.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.s21school.retailanalytics_web.dto.viewDto.GroupViewCsvDto;
import ru.s21school.retailanalytics_web.dto.viewDto.GroupViewDto;

@Mapper(componentModel = "spring")
public interface GroupViewMapper {
    @Mapping(target = "customerId", source = "customer.id")
    @Mapping(target = "skuGroupId", source = "skuGroup.id")
    GroupViewCsvDto map(GroupViewDto dto);
}
