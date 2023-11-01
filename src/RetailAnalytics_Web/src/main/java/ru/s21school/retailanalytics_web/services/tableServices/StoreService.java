package ru.s21school.retailanalytics_web.services.tableServices;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import ru.s21school.retailanalytics_web.controllers.tableControllers.ImportExportHandler;
import ru.s21school.retailanalytics_web.dto.entityDto.storeDto.StoreCreateDto;
import ru.s21school.retailanalytics_web.dto.entityDto.storeDto.StorePageDto;
import ru.s21school.retailanalytics_web.dto.entityDto.storeDto.StoreReadDto;
import ru.s21school.retailanalytics_web.exceptions.EmptyResponseBodyException;

import java.io.IOException;

@Service
@Slf4j
public class StoreService {
    private static final String STORES_API_URL = "http://localhost:8081/api/v1/stores";
    private final ImportExportHandler importExportHandler;
    private final RestTemplate restTemplate;
    private final String PAGE_URL_TEMPLATE;
    private final String ID_URL_TEMPLATE;

    public StoreService(RestTemplate restTemplate) {
        this.importExportHandler = new ImportExportHandler(STORES_API_URL, "stores", restTemplate);
        this.restTemplate = restTemplate;
        PAGE_URL_TEMPLATE = STORES_API_URL + "?page=%d&size=%d";
        ID_URL_TEMPLATE = STORES_API_URL + "/%d/%d";
    }

    public StorePageDto performGetPageRequest(int page, int size) {
        final String URL_REQUEST = String.format(PAGE_URL_TEMPLATE, page, size);
        ResponseEntity<StorePageDto> response =
                restTemplate.exchange(URL_REQUEST,
                        HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                        });
        StorePageDto body = response.getBody();
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
        importExportHandler.exportToCsv(servletResponse);
    }

    public void performImportFromCsv(MultipartFile file) {
        importExportHandler.importFromCsv(file);
    }
}
