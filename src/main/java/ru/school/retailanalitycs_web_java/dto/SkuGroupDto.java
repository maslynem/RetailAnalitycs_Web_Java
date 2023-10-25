package ru.school.retailanalitycs_web_java.dto;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SkuGroupDto {
    @CsvBindByName(column = "group_id", required = true)
    @CsvBindByPosition(position = 0)
    private Integer id;

    @CsvBindByName(column = "group_name", required = true)
    @CsvBindByPosition(position = 1)
    private String groupName;
}
