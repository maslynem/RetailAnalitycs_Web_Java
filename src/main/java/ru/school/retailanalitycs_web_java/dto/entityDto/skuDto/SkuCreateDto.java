package ru.school.retailanalitycs_web_java.dto.entityDto.skuDto;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;
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
    @CsvBindByName(column = "sku_id", required = true)
    @CsvBindByPosition(position = 0)
    private Long id;

    @NotBlank(message = "Sku name is mandatory")
    @CsvBindByName(column = "sku_name", required = true)
    @CsvBindByPosition(position = 1)
    private String skuName;

    @NotNull(message = "Sku group is mandatory")
    @CsvBindByName(column = "group_id", required = true)
    @CsvBindByPosition(position = 2)
    private Long skuGroup;
}
