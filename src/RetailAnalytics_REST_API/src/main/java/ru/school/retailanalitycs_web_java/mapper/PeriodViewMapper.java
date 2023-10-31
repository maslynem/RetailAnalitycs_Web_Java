package ru.school.retailanalitycs_web_java.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.school.retailanalitycs_web_java.dto.viewDto.PeriodViewDto;
import ru.school.retailanalitycs_web_java.entities.views.periodView.PeriodView;

@Mapper(componentModel = "spring", uses = {CustomerMapper.class, SkuGroupMapper.class})
public abstract class PeriodViewMapper {

    @Mapping(target = "skuGroup", source = "id.skuGroup")
    @Mapping(target = "customer", source = "id.customer")
    public abstract PeriodViewDto toDto(PeriodView periodView);

}