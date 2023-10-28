package ru.s21school.retailanalytics_web.dto.entityDto.skuDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.s21school.retailanalytics_web.dto.entityDto.skuGroupDto.SkuGroupDto;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SkuReadDto {
    private Long id;
    private String skuName;
    private SkuGroupDto skuGroup;
}
