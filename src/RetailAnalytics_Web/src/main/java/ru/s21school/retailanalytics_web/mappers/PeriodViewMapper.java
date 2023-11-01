package ru.s21school.retailanalytics_web.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.s21school.retailanalytics_web.dto.viewDto.PeriodViewCsvDto;
import ru.s21school.retailanalytics_web.dto.viewDto.PeriodViewDto;

@Mapper(componentModel = "spring")
public interface PeriodViewMapper {
    @Mapping(target = "customerId", source = "customer.id")
    @Mapping(target = "skuGroupId", source = "skuGroup.id")
    PeriodViewCsvDto map(PeriodViewDto dto);
}
