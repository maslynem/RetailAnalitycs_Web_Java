package ru.s21school.retailanalytics_web.controllers.operationsController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import ru.s21school.retailanalytics_web.controllers.operationsController.requestEntity.SqlQueryRequest;
import ru.s21school.retailanalytics_web.dto.ErrorDto;
import ru.s21school.retailanalytics_web.services.SqlQueryService;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("query")
@RequiredArgsConstructor
@Slf4j
public class SqlQueryController {

    private final SqlQueryService sqlQueryService;

    @ExceptionHandler(HttpClientErrorException.class)
    public String handleSQLGrammarException(HttpClientErrorException exception, Model model) {
        ErrorDto errorDto = exception.getResponseBodyAs(ErrorDto.class);
        log.warn("handle exception: HttpClientErrorException. Code: {}. Message: {}", errorDto.getCode(), errorDto.getMessages().toString());
        model.addAttribute("errors", errorDto.getMessages());
        return "operations/query";
    }

    @GetMapping
    public String getFunctionsPage() {
        return "operations/query";
    }

    @PostMapping
    public String getFrequencyOfVisit(@ModelAttribute SqlQueryRequest request, Model model) {
        List<Map<String, Object>> frequencyOfVisit = sqlQueryService.getSqlQueryResult(request);
        model.addAttribute("entities", frequencyOfVisit);
        return "operations/query";
    }
}
