package ru.school.retailanalitycs_web_java.dto.entityDto.skuDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SkuCreateDto {
    private Long id;

    @NotBlank(message = "Sku name is mandatory")
    private String skuName;

    @NotNull(message = "Sku group is mandatory")
    private Long skuGroup;
}
