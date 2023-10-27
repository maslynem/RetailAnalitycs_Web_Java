package ru.school.retailanalitycs_web_java.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.school.retailanalitycs_web_java.dto.functionResultDto.FrequencyOfVisitDto;
import ru.school.retailanalitycs_web_java.entities.functionResults.FrequencyOfVisit;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING, uses = {CustomerMapper.class})
public abstract class FrequencyOfVisitMapper {

    @Mapping(target = "customer", source = "customerId")
    public abstract FrequencyOfVisitDto toDto(FrequencyOfVisit entity);
}
