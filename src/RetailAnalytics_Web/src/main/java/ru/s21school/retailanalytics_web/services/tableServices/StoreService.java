package ru.s21school.retailanalytics_web.services.tableServices;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.s21school.retailanalytics_web.dto.PageDto;
import ru.s21school.retailanalytics_web.dto.entityDto.storeDto.StoreCreateDto;
import ru.s21school.retailanalytics_web.dto.entityDto.storeDto.StoreReadDto;
import ru.s21school.retailanalytics_web.exceptions.EmptyResponseBodyException;
import ru.s21school.retailanalytics_web.mappers.StoreMapper;
import ru.s21school.retailanalytics_web.utils.CsvReader;
import ru.s21school.retailanalytics_web.utils.CsvWriter;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
@Slf4j
public class StoreService {
    private static final String STORES_API_URL = "http://retailAnalyticsApi/api/v1/stores";
    private final RestTemplate restTemplate;
    private final String PAGE_URL_TEMPLATE;
    private final String ID_URL_TEMPLATE;
    private final StoreMapper storeMapper;
    private final CsvReader<StoreCreateDto> csvReader;
    private final CsvWriter<StoreCreateDto> csvWriter;

    public StoreService(RestTemplate restTemplate, StoreMapper storeMapper, CsvReader<StoreCreateDto> csvReader, CsvWriter<StoreCreateDto> csvWriter) {
        this.restTemplate = restTemplate;
        this.storeMapper = storeMapper;
        this.csvReader = csvReader;
        this.csvWriter = csvWriter;
        PAGE_URL_TEMPLATE = STORES_API_URL + "?page=%d&size=%d";
        ID_URL_TEMPLATE = STORES_API_URL + "/%d/%d";
    }

    public List<StoreReadDto> performGetAll() {
        ResponseEntity<List<StoreReadDto>> response =
                restTemplate.exchange(STORES_API_URL,
                        HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                        });
        List<StoreReadDto> body = response.getBody();
        if (body == null) {
            log.error("Error after request to [{}]. Response status code is [{}]. But response body is null", STORES_API_URL, response.getStatusCode());
            throw new EmptyResponseBodyException();
        }
        return body;
    }

    public PageDto<StoreReadDto> performGetPageRequest(int page, int size) {
        final String URL_REQUEST = String.format(PAGE_URL_TEMPLATE, page, size);
        ResponseEntity<PageDto<StoreReadDto>> response =
                restTemplate.exchange(URL_REQUEST,
                        HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                        });
        PageDto<StoreReadDto> body = response.getBody();
        if (body == null) {
            log.error("Error after request to [{}]. Response status code is [{}]. But response body is null", URL_REQUEST, response.getStatusCode());
            throw new EmptyResponseBodyException();
        }
        return body;
    }

    public StoreReadDto performFindByIdRequest(Long trStoreId, Long skuId) {
        final String URL_REQUEST = String.format(ID_URL_TEMPLATE, trStoreId, skuId);
        ResponseEntity<StoreReadDto> response =
                restTemplate.exchange(URL_REQUEST,
                        HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                        });
        StoreReadDto body = response.getBody();
        if (body == null) {
            log.error("Error after request to [{}]. Response status code is [{}]. But response body is null", URL_REQUEST, response.getStatusCode());
            throw new EmptyResponseBodyException();
        }
        return body;
    }

    public void performSaveStoreRequest(StoreCreateDto store) {
        HttpEntity<StoreCreateDto> httpEntity = new HttpEntity<>(store);
        ResponseEntity<StoreReadDto> response =
                restTemplate.exchange(STORES_API_URL,
                        HttpMethod.POST, httpEntity, new ParameterizedTypeReference<>() {
                        });
        StoreReadDto body = response.getBody();
        if (body == null) {
            log.error("Error after request to [{}]. Response status code is [{}]. But response body is null", STORES_API_URL, response.getStatusCode());
            throw new EmptyResponseBodyException();
        }
        log.info("New store was saved: {}", body);
    }

    public void performSaveStoresRequest(List<StoreCreateDto> stores) {
        HttpEntity<List<StoreCreateDto>> httpEntity = new HttpEntity<>(stores);
        restTemplate.exchange(STORES_API_URL + "/all",
                HttpMethod.POST, httpEntity, new ParameterizedTypeReference<>() {
                });
        log.info("Stores was saved. Size: {}", stores.size());
    }
    
    public void performUpdateStoreRequest(Long trStoreId, Long skuId, StoreCreateDto store) {
        final String URL_REQUEST = String.format(ID_URL_TEMPLATE, trStoreId, skuId);
        HttpEntity<StoreCreateDto> httpEntity = new HttpEntity<>(store);
        ResponseEntity<StoreReadDto> response =
                restTemplate.exchange(URL_REQUEST,
                        HttpMethod.PUT, httpEntity, new ParameterizedTypeReference<>() {
                        });
        StoreReadDto body = response.getBody();
        if (body == null) {
            log.error("Error after request to [{}]. Response status code is [{}]. But response body is null", URL_REQUEST, response.getStatusCode());
            throw new EmptyResponseBodyException();
        }
        log.info("Store was updated.{}", body);
    }

    public void performDeleteByIdRequest(Long trStoreId, Long skuId) {
        restTemplate.delete(String.format(ID_URL_TEMPLATE, trStoreId, skuId));
    }

    public void performExportToCsv(HttpServletResponse servletResponse) throws IOException {
        List<StoreCreateDto> stores = performGetAll().stream().map(storeMapper::map).toList();
        servletResponse.setContentType("text/csv");
        servletResponse.addHeader("Content-Disposition", "attachment; filename=stores.csv");
        servletResponse.setCharacterEncoding("UTF-8");
        csvWriter.exportCsv(servletResponse.getWriter(), stores, StoreCreateDto.class);
    }

    public void performImportFromCsv(InputStream is) {
        List<StoreCreateDto> customers = csvReader.importCsv(is, StoreCreateDto.class);
        performSaveStoresRequest(customers);
    }
}
