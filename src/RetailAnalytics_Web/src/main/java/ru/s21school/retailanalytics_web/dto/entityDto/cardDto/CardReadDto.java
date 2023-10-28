package ru.s21school.retailanalytics_web.dto.entityDto.cardDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.s21school.retailanalytics_web.dto.entityDto.customerDto.CustomerDto;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CardReadDto {
    private Long id;
    private CustomerDto customer;
}
