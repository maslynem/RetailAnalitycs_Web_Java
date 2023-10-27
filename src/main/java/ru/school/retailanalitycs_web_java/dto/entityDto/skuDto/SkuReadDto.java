package ru.school.retailanalitycs_web_java.dto.entityDto.skuDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.school.retailanalitycs_web_java.dto.entityDto.skuGroupDto.SkuGroupDto;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SkuReadDto {
    private Integer id;
    private String skuName;
    private SkuGroupDto skuGroup;
}
