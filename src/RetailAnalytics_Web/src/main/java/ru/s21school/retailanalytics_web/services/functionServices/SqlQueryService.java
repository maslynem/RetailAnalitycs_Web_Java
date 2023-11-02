package ru.s21school.retailanalytics_web.services.functionServices;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import ru.s21school.retailanalytics_web.controllers.operationsController.requestEntity.SqlQueryRequest;
import ru.s21school.retailanalytics_web.exceptions.EmptyResponseBodyException;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class SqlQueryService {
    private static final String SQL_QUERY_API_URL = "http://localhost:8081/api/v1/query";

    private final RestTemplate restTemplate;

    public List<Map<String, Object>> getSqlQueryResult(SqlQueryRequest request) throws HttpClientErrorException {
        HttpEntity<SqlQueryRequest> httpEntity = new HttpEntity<>(request);
        ResponseEntity<List<Map<String, Object>>> response =
                restTemplate.exchange(SQL_QUERY_API_URL,
                        HttpMethod.POST, httpEntity, new ParameterizedTypeReference<>() {
                        });
        List<Map<String, Object>> body = response.getBody();
        if (body == null) {
            log.error("Error after request to [{} frequency-of-visits]. Response status code is [{}]. But response body is null", SQL_QUERY_API_URL, response.getStatusCode());
            throw new EmptyResponseBodyException();
        }
        return body;
    }
}
