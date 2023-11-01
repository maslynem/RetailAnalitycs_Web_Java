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
import ru.s21school.retailanalytics_web.dto.viewDto.PurchaseHistoryViewCsvDto;
import ru.s21school.retailanalytics_web.dto.viewDto.PurchaseHistoryViewDto;
import ru.s21school.retailanalytics_web.exceptions.EmptyResponseBodyException;
import ru.s21school.retailanalytics_web.mappers.PurchaseHistoryViewMapper;
import ru.s21school.retailanalytics_web.utils.CsvWriter;

import java.io.IOException;
import java.util.List;

@Service
@Slf4j
public class PurchaseHistoryViewService {
    private static final String PURCHASE_HISTORY_VIEW_API_URL = "http://localhost:8081/api/v1/views/purchase-histories";
    private final String PAGE_URL_TEMPLATE;

    private final RestTemplate restTemplate;
    private final CsvWriter<PurchaseHistoryViewCsvDto> csvWriter;
    private final PurchaseHistoryViewMapper purchaseHistoryViewMapper;

    @Autowired
    public PurchaseHistoryViewService(RestTemplate restTemplate, CsvWriter<PurchaseHistoryViewCsvDto> csvWriter, PurchaseHistoryViewMapper purchaseHistoryViewMapper) {
        this.restTemplate = restTemplate;
        this.csvWriter = csvWriter;
        this.purchaseHistoryViewMapper = purchaseHistoryViewMapper;
        PAGE_URL_TEMPLATE = PURCHASE_HISTORY_VIEW_API_URL + "?page=%d&size=%d";
    }

    public List<PurchaseHistoryViewDto> performGetAll() {
        ResponseEntity<List<PurchaseHistoryViewDto>> response =
                restTemplate.exchange(PURCHASE_HISTORY_VIEW_API_URL,
                        HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                        });
        List<PurchaseHistoryViewDto> body = response.getBody();
        if (body == null) {
            log.error("Error after request to [{}]. Response status code is [{}]. But response body is null", PURCHASE_HISTORY_VIEW_API_URL, response.getStatusCode());
            throw new EmptyResponseBodyException();
        }
        return body;
    }

    public PageDto<PurchaseHistoryViewDto> performGetPageRequest(int page, int size) {
        final String URL_REQUEST = String.format(PAGE_URL_TEMPLATE, page, size);
        ResponseEntity<PageDto<PurchaseHistoryViewDto>> response =
                restTemplate.exchange(URL_REQUEST,
                        HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                        });
        PageDto<PurchaseHistoryViewDto> body = response.getBody();
        if (body == null) {
            log.error("Error after request to [{}]. Response status code is [{}]. But response body is null", URL_REQUEST, response.getStatusCode());
            throw new EmptyResponseBodyException();
        }
        return body;
    }

    public void performExportToCsv(HttpServletResponse servletResponse) throws IOException {
        List<PurchaseHistoryViewCsvDto> purchaseHistorys = performGetAll().stream().map(purchaseHistoryViewMapper::map).toList();
        servletResponse.setContentType("text/csv");
        servletResponse.addHeader("Content-Disposition", "attachment; filename=purchaseHistories.csv");
        servletResponse.setCharacterEncoding("UTF-8");
        csvWriter.exportCsv(servletResponse.getWriter(), purchaseHistorys, PurchaseHistoryViewCsvDto.class);
    }
}
