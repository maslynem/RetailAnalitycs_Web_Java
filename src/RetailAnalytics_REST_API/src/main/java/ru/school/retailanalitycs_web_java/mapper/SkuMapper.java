package ru.school.retailanalitycs_web_java.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import ru.school.retailanalitycs_web_java.dto.entityDto.skuDto.SkuCreateDto;
import ru.school.retailanalitycs_web_java.dto.entityDto.skuDto.SkuReadDto;
import ru.school.retailanalitycs_web_java.entities.tables.Sku;
import ru.school.retailanalitycs_web_java.exceptions.notFoundExceptions.SkuNotFoundException;
import ru.school.retailanalitycs_web_java.services.entityServices.SkuService;

@Mapper(componentModel = "spring", uses = SkuGroupMapper.class)
public abstract class SkuMapper {
    @Autowired
    private SkuService skuService;

    public abstract SkuReadDto toDto(Sku entity);

    @Mapping(target = "skuGroup", source = "skuGroup.id")
    public abstract SkuCreateDto toCreateDto(Sku sku);

    @Mapping(target = "id", ignore = true)
    public abstract Sku toEntity(SkuCreateDto dto);

    public Sku toDtoById(Long id) {
        return skuService.findById(id).orElseThrow(() -> new SkuNotFoundException(id));
    }

    public Long map(Sku value) {
        return value.getId();
    }

}
