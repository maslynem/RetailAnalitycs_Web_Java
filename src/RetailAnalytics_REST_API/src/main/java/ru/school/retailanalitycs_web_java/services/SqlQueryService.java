package ru.school.retailanalitycs_web_java.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class SqlQueryService {
    private final JdbcTemplate jdbcTemplate;

    public List<Map<String, Object>> executeQuery(String sql) {
        log.info("executeQuery called. SQL query: {}", sql);
        return jdbcTemplate.queryForList(sql);
    }
}
