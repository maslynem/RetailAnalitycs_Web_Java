package ru.s21school.retailanalytics_web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageDto<E> {
    private List<E> content = Collections.emptyList();
    private int totalPages = 0;
    private int totalElements = 0;
}
