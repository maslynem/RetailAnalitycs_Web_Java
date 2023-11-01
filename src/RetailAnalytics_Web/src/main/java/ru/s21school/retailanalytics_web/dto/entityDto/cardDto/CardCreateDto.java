package ru.s21school.retailanalytics_web.dto.entityDto.cardDto;

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
    private Long customer;
}
