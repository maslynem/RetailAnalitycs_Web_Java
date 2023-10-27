package ru.school.retailanalitycs_web_java.dto.entityDto.cardDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.school.retailanalitycs_web_java.dto.entityDto.customerDto.CustomerDto;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CardReadDto {

    private Integer id;

    private CustomerDto customer;
}
