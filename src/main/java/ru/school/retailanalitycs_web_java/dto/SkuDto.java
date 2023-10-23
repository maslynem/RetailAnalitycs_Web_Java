package ru.school.retailanalitycs_web_java.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;

@Data
@EqualsAndHashCode(callSuper = false)
public class SkuDto extends BaseDto<Integer> {
    @CsvBindByName(column = "sku_id", required = true)
    @CsvBindByPosition(position = 0)
    private Integer id;

    @CsvBindByName(column = "sku_name", required = true)
    @CsvBindByPosition(position = 1)
    private String skuName;

    @CsvBindByName(column = "group_id", required = true)
    @CsvBindByPosition(position = 2)
    private Integer skuGroup;
}
