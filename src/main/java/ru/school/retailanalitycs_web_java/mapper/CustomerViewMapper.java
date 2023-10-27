package ru.school.retailanalitycs_web_java.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.school.retailanalitycs_web_java.dto.viewDto.CustomerViewDto;
import ru.school.retailanalitycs_web_java.entities.views.CustomerView;

@Mapper(componentModel = "spring", uses = {CustomerMapper.class})
public abstract class CustomerViewMapper {

    @Mapping(target = "customer", source = "customerId")
    public abstract CustomerViewDto toDto(CustomerView entity);
}
