package ru.s21school.retailanalytics_web.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.s21school.retailanalytics_web.dto.functionResultDto.FrequencyOfVisitCsvDto;
import ru.s21school.retailanalytics_web.dto.functionResultDto.FrequencyOfVisitDto;

@Mapper(componentModel = "spring")
public interface FrequencyOfVisitMapper {
    @Mapping(target = "customerId", source = "customer.id")
    FrequencyOfVisitCsvDto map(FrequencyOfVisitDto dto);
}
