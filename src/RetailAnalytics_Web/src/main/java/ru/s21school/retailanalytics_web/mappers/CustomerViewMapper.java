package ru.s21school.retailanalytics_web.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.s21school.retailanalytics_web.dto.viewDto.CustomerViewCsvDto;
import ru.s21school.retailanalytics_web.dto.viewDto.CustomerViewDto;

@Mapper(componentModel = "spring")
public interface CustomerViewMapper {
    @Mapping(target = "customerId", source = "customer.id")
    CustomerViewCsvDto map(CustomerViewDto dto);
}
