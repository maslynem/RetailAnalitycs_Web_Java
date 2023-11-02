package ru.school.retailanalitycs_web_java.services.functionServices;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class SqlQueryService {
    private final JdbcTemplate jdbcTemplate;

    public List<Map<String, Object>> executeQuery(String query) {
        List<Map<String, Object>> result = null;
        int updateResult;
        if (query.toLowerCase().contains("select")) {
            result = executeSelectQuery(query);
            updateResult = result.size();
        } else {
            updateResult = executeUpdateQuery(query);
        }
        if (result == null) {
            result = new ArrayList<>();
            result.add(Map.of("updateCount", updateResult));
        }
        return result;
    }

    private List<Map<String, Object>> executeSelectQuery(String sql) {
        log.info("executeSelectQuery called. SQL query: {}", sql);
        return jdbcTemplate.queryForList(sql);
    }

    private int executeUpdateQuery(String sql) {
        log.info("executeUpdateQuery called. SQL query: {}", sql);
        return jdbcTemplate.update(sql);
    }


}
