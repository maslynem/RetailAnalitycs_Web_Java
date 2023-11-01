package ru.school.retailanalitycs_web_java.dto.entityDto.cardDto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CardCreateDto {

    private Long id;

    @NotNull(message = "Customer is mandatory")
    private Long customer;
}
