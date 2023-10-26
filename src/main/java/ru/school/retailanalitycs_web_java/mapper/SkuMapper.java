package ru.school.retailanalitycs_web_java.mapper;

import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import ru.school.retailanalitycs_web_java.dto.skuDto.SkuCreateDto;
import ru.school.retailanalitycs_web_java.dto.skuDto.SkuReadDto;
import ru.school.retailanalitycs_web_java.entities.tables.Sku;
import ru.school.retailanalitycs_web_java.exceptions.notFoundExceptions.SkuNotFoundException;
import ru.school.retailanalitycs_web_java.services.SkuService;

@Mapper(componentModel = "spring", uses = SkuGroupMapper.class)
public abstract class SkuMapper {
    @Autowired
    private SkuService skuService;

    public abstract SkuReadDto toDto(Sku entity);

    public abstract Sku toEntity(SkuCreateDto dto);

    public Sku toDtoById(Integer id) {
        return skuService.findById(id).orElseThrow(() -> new SkuNotFoundException(id));
    }
}
