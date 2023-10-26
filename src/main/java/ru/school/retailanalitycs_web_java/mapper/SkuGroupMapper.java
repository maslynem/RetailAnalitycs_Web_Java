package ru.school.retailanalitycs_web_java.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import ru.school.retailanalitycs_web_java.dto.skuGroupDto.SkuGroupDto;
import ru.school.retailanalitycs_web_java.entities.tables.SkuGroup;
import ru.school.retailanalitycs_web_java.exceptions.notFoundExceptions.SkuGroupNotFoundException;
import ru.school.retailanalitycs_web_java.services.SkuGroupService;

@Mapper(componentModel = "spring")
public abstract class SkuGroupMapper {
    @Autowired
    private SkuGroupService skuGroupService;

    public abstract SkuGroupDto toDto(SkuGroup entity);

    @Mapping(target = "skus", ignore = true)
    public abstract SkuGroup toEntity(SkuGroupDto dto);

    public SkuGroup toDtoById(Integer skuGroupId) {
        return skuGroupService.findById(skuGroupId).orElseThrow(() -> new SkuGroupNotFoundException(skuGroupId));
    }
}
