package ru.s21school.retailanalytics_web.services.viewServices;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.s21school.retailanalytics_web.dto.PageDto;
import ru.s21school.retailanalytics_web.dto.viewDto.PeriodViewCsvDto;
import ru.s21school.retailanalytics_web.dto.viewDto.PeriodViewDto;
import ru.s21school.retailanalytics_web.exceptions.EmptyResponseBodyException;
import ru.s21school.retailanalytics_web.mappers.PeriodViewMapper;
import ru.s21school.retailanalytics_web.utils.CsvWriter;

import java.io.IOException;
import java.util.List;

@Service
@Slf4j
public class PeriodViewService {
    private static final String PERIOD_VIEW_API_URL = "http://retailAnalyticsApi/api/v1/views/periods";
    private final String PAGE_URL_TEMPLATE;

    private final RestTemplate restTemplate;
    private final CsvWriter<PeriodViewCsvDto> csvWriter;
    private final PeriodViewMapper periodViewMapper;

    @Autowired
    public PeriodViewService(RestTemplate restTemplate, CsvWriter<PeriodViewCsvDto> csvWriter, PeriodViewMapper periodViewMapper) {
        this.restTemplate = restTemplate;
        this.csvWriter = csvWriter;
        this.periodViewMapper = periodViewMapper;
        PAGE_URL_TEMPLATE = PERIOD_VIEW_API_URL + "?page=%d&size=%d";
    }

    public List<PeriodViewDto> performGetAll() {
        ResponseEntity<List<PeriodViewDto>> response =
                restTemplate.exchange(PERIOD_VIEW_API_URL,
                        HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                        });
        List<PeriodViewDto> body = response.getBody();
        if (body == null) {
            log.error("Error after request to [{}]. Response status code is [{}]. But response body is null", PERIOD_VIEW_API_URL, response.getStatusCode());
            throw new EmptyResponseBodyException();
        }
        return body;
    }

    public PageDto<PeriodViewDto> performGetPageRequest(int page, int size) {
        final String URL_REQUEST = String.format(PAGE_URL_TEMPLATE, page, size);
        ResponseEntity<PageDto<PeriodViewDto>> response =
                restTemplate.exchange(URL_REQUEST,
                        HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                        });
        PageDto<PeriodViewDto> body = response.getBody();
        if (body == null) {
            log.error("Error after request to [{}]. Response status code is [{}]. But response body is null", URL_REQUEST, response.getStatusCode());
            throw new EmptyResponseBodyException();
        }
        return body;
    }

    public void performExportToCsv(HttpServletResponse servletResponse) throws IOException {
        List<PeriodViewCsvDto> periods = performGetAll().stream().map(periodViewMapper::map).toList();
        servletResponse.setContentType("text/csv");
        servletResponse.addHeader("Content-Disposition", "attachment; filename=periods.csv");
        servletResponse.setCharacterEncoding("UTF-8");
        csvWriter.exportCsv(servletResponse.getWriter(), periods, PeriodViewCsvDto.class);
    }
}
