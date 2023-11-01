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
import ru.s21school.retailanalytics_web.dto.viewDto.GroupViewCsvDto;
import ru.s21school.retailanalytics_web.dto.viewDto.GroupViewDto;
import ru.s21school.retailanalytics_web.exceptions.EmptyResponseBodyException;
import ru.s21school.retailanalytics_web.mappers.GroupViewMapper;
import ru.s21school.retailanalytics_web.utils.CsvWriter;

import java.io.IOException;
import java.util.List;

@Service
@Slf4j
public class GroupViewService {
    private static final String GROUP_VIEW_API_URL = "http://localhost:8081/api/v1/views/groups";
    private final String PAGE_URL_TEMPLATE;

    private final RestTemplate restTemplate;
    private final CsvWriter<GroupViewCsvDto> csvWriter;
    private final GroupViewMapper groupViewMapper;

    @Autowired
    public GroupViewService(RestTemplate restTemplate, CsvWriter<GroupViewCsvDto> csvWriter, GroupViewMapper groupViewMapper) {
        this.restTemplate = restTemplate;
        this.csvWriter = csvWriter;
        this.groupViewMapper = groupViewMapper;
        PAGE_URL_TEMPLATE = GROUP_VIEW_API_URL + "?page=%d&size=%d";
    }

    public List<GroupViewDto> performGetAll() {
        ResponseEntity<List<GroupViewDto>> response =
                restTemplate.exchange(GROUP_VIEW_API_URL,
                        HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                        });
        List<GroupViewDto> body = response.getBody();
        if (body == null) {
            log.error("Error after request to [{}]. Response status code is [{}]. But response body is null", GROUP_VIEW_API_URL, response.getStatusCode());
            throw new EmptyResponseBodyException();
        }
        return body;
    }

    public PageDto<GroupViewDto> performGetPageRequest(int page, int size) {
        final String URL_REQUEST = String.format(PAGE_URL_TEMPLATE, page, size);
        ResponseEntity<PageDto<GroupViewDto>> response =
                restTemplate.exchange(URL_REQUEST,
                        HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                        });
        PageDto<GroupViewDto> body = response.getBody();
        if (body == null) {
            log.error("Error after request to [{}]. Response status code is [{}]. But response body is null", URL_REQUEST, response.getStatusCode());
            throw new EmptyResponseBodyException();
        }
        return body;
    }

    public void performExportToCsv(HttpServletResponse servletResponse) throws IOException {
        List<GroupViewCsvDto> groups = performGetAll().stream().map(groupViewMapper::map).toList();
        servletResponse.setContentType("text/csv");
        servletResponse.addHeader("Content-Disposition", "attachment; filename=groups.csv");
        servletResponse.setCharacterEncoding("UTF-8");
        csvWriter.exportCsv(servletResponse.getWriter(), groups, GroupViewCsvDto.class);
    }
}
