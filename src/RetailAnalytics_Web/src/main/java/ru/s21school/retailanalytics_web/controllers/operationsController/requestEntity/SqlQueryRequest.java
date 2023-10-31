package ru.s21school.retailanalytics_web.controllers.operationsController.requestEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SqlQueryRequest {
    private String query;
}
