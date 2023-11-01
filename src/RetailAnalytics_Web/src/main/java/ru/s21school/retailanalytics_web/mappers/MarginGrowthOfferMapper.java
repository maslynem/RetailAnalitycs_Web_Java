package ru.s21school.retailanalytics_web.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.s21school.retailanalytics_web.dto.functionResultDto.MarginGrowthOfferCsvDto;
import ru.s21school.retailanalytics_web.dto.functionResultDto.MarginGrowthOfferDto;

@Mapper(componentModel = "spring")
public interface MarginGrowthOfferMapper {
    @Mapping(target = "customerId", source = "customer.id")
    MarginGrowthOfferCsvDto map(MarginGrowthOfferDto dto);
}
