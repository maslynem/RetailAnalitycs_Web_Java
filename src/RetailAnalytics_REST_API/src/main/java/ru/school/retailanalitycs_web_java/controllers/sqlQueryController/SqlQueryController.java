package ru.school.retailanalitycs_web_java.controllers.sqlQueryController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.school.retailanalitycs_web_java.services.functionServices.SqlQueryService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/query")
@RequiredArgsConstructor
@Slf4j
public class SqlQueryController {
    private final SqlQueryService sqlQueryService;

    @PostMapping
    public List<Map<String, Object>> executeQuery(@RequestBody SqlQueryRequest request) {
        return sqlQueryService.executeQuery(request.getQuery());
    }
}
