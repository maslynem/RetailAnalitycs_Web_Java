package ru.school.retailanalitycs_web_java.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.school.retailanalitycs_web_java.dto.functionResultDto.PersonalOfferDto;
import ru.school.retailanalitycs_web_java.entities.functionResults.PersonalOffer;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING, uses = {CustomerMapper.class})
public abstract class PersonalOfferMapper {

    @Mapping(target = "customer", source = "customerId")
    public abstract PersonalOfferDto toDto(PersonalOffer entity);
}
