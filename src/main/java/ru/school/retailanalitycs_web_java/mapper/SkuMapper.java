package ru.school.retailanalitycs_web_java.mapper;

import org.mapstruct.Mapper;
import ru.school.retailanalitycs_web_java.dto.skuDto.SkuCreateDto;
import ru.school.retailanalitycs_web_java.dto.skuDto.SkuReadDto;
import ru.school.retailanalitycs_web_java.entities.tables.Sku;

@Mapper(componentModel = "spring", uses = SkuGroupMapper.class)
public abstract class SkuMapper {
    public abstract SkuReadDto toDto(Sku skuGroup);

    public abstract Sku toEntity(SkuCreateDto dto);

//    public SkuGroup toDtoById(Integer skuGroupId) {
//        return skuGroupService.findById(skuGroupId).orElseThrow(() -> new SkuGroupNotFoundException(skuGroupId));
//    }
}
