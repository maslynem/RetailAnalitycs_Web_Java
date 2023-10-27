package ru.school.retailanalitycs_web_java.dto.entityDto.skuGroupDto;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SkuGroupDto {
    @CsvBindByName(column = "group_id", required = true)
    @CsvBindByPosition(position = 0)
    private Integer id;

    @NotNull(message = "Group Name is mandatory")
    @CsvBindByName(column = "group_name", required = true)
    @CsvBindByPosition(position = 1)
    private String groupName;
}
