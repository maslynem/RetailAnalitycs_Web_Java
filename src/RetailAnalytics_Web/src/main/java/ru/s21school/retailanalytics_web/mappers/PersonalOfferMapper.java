package ru.s21school.retailanalytics_web.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.s21school.retailanalytics_web.dto.functionResultDto.PersonalOfferCsvDto;
import ru.s21school.retailanalytics_web.dto.functionResultDto.PersonalOfferDto;

@Mapper(componentModel = "spring")
public interface PersonalOfferMapper {
    @Mapping(target = "customerId", source = "customer.id")
    PersonalOfferCsvDto map(PersonalOfferDto dto);
}
