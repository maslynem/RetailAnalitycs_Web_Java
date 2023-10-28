package ru.school.retailanalitycs_web_java.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.school.retailanalitycs_web_java.dto.functionResultDto.MarginGrowthOfferDto;
import ru.school.retailanalitycs_web_java.entities.functionResults.MarginGrowthOffer;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING, uses = {CustomerMapper.class})
public abstract class MarginGrowthOfferMapper {

    @Mapping(target = "customer", source = "customerId")
    public abstract MarginGrowthOfferDto toDto(MarginGrowthOffer entity);
}
