package ru.school.retailanalitycs_web_java.dto.entityDto.skuGroupDto;

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

    private Long id;

    @NotNull(message = "Group Name is mandatory")

    private String groupName;
}
