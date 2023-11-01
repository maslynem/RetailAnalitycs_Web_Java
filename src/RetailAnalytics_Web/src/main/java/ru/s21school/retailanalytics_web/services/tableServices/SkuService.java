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
import ru.s21school.retailanalytics_web.dto.entityDto.skuDto.SkuCreateDto;
import ru.s21school.retailanalytics_web.dto.entityDto.skuDto.SkuReadDto;
import ru.s21school.retailanalytics_web.exceptions.EmptyResponseBodyException;
import ru.s21school.retailanalytics_web.mappers.SkuMapper;
import ru.s21school.retailanalytics_web.utils.CsvReader;
import ru.s21school.retailanalytics_web.utils.CsvWriter;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
@Slf4j
public class SkuService {
    private static final String SKU_API_URL = "http://localhost:8081/api/v1/skus";
    private final RestTemplate restTemplate;
    private final String PAGE_URL_TEMPLATE;
    private final String ID_URL_TEMPLATE;
    private final SkuMapper skuMapper;
    private final CsvReader<SkuCreateDto> csvReader;
    private final CsvWriter<SkuCreateDto> csvWriter;

    public SkuService(RestTemplate restTemplate, SkuMapper skuMapper, CsvReader<SkuCreateDto> csvReader, CsvWriter<SkuCreateDto> csvWriter) {
        this.restTemplate = restTemplate;
        this.skuMapper = skuMapper;
        this.csvReader = csvReader;
        this.csvWriter = csvWriter;
        PAGE_URL_TEMPLATE = SKU_API_URL + "?page=%d&size=%d";
        ID_URL_TEMPLATE = SKU_API_URL + "/%d";
    }

    public List<SkuReadDto> performGetAll() {
        ResponseEntity<List<SkuReadDto>> response =
                restTemplate.exchange(SKU_API_URL,
                        HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                        });
        List<SkuReadDto> body = response.getBody();
        if (body == null) {
            log.error("Error after request to [{}]. Response status code is [{}]. But response body is null", SKU_API_URL, response.getStatusCode());
            throw new EmptyResponseBodyException();
        }
        return body;
    }

    public PageDto<SkuReadDto> performGetPageRequest(int page, int size) {
        final String URL_REQUEST = String.format(PAGE_URL_TEMPLATE, page, size);
        ResponseEntity<PageDto<SkuReadDto>> response =
                restTemplate.exchange(URL_REQUEST,
                        HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                        });
        PageDto<SkuReadDto> body = response.getBody();
        if (body == null) {
            log.error("Error after request to [{}]. Response status code is [{}]. But response body is null", URL_REQUEST, response.getStatusCode());
            throw new EmptyResponseBodyException();
        }
        return body;
    }

    public SkuReadDto performFindByIdRequest(Long id) {
        final String URL_REQUEST = String.format(ID_URL_TEMPLATE, id);
        ResponseEntity<SkuReadDto> response =
                restTemplate.exchange(URL_REQUEST,
                        HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                        });
        SkuReadDto body = response.getBody();
        if (body == null) {
            log.error("Error after request to [{}]. Response status code is [{}]. But response body is null", URL_REQUEST, response.getStatusCode());
            throw new EmptyResponseBodyException();
        }
        return body;
    }

    public void performSaveSkuRequest(SkuCreateDto sku) {
        HttpEntity<SkuCreateDto> httpEntity = new HttpEntity<>(sku);
        ResponseEntity<SkuReadDto> response =
                restTemplate.exchange(SKU_API_URL,
                        HttpMethod.POST, httpEntity, new ParameterizedTypeReference<>() {
                        });
        SkuReadDto body = response.getBody();
        if (body == null) {
            log.error("Error after request to [{}]. Response status code is [{}]. But response body is null", SKU_API_URL, response.getStatusCode());
            throw new EmptyResponseBodyException();
        }
        log.info("New sku was saved. Id: {}", body.getId());
    }

    public void performSaveSkusRequest(List<SkuCreateDto> skus) {
        HttpEntity<List<SkuCreateDto>> httpEntity = new HttpEntity<>(skus);
        restTemplate.exchange(SKU_API_URL + "/all",
                HttpMethod.POST, httpEntity, new ParameterizedTypeReference<>() {
                });
        log.info("Skus was saved. Size: {}", skus.size());
    }
    
    public void performUpdateSkuRequest(Long id, SkuCreateDto sku) {
        final String URL_REQUEST = String.format(ID_URL_TEMPLATE, id);
        HttpEntity<SkuCreateDto> httpEntity = new HttpEntity<>(sku);
        ResponseEntity<SkuReadDto> response =
                restTemplate.exchange(URL_REQUEST,
                        HttpMethod.PUT, httpEntity, new ParameterizedTypeReference<>() {
                        });
        SkuReadDto body = response.getBody();
        if (body == null) {
            log.error("Error after request to [{}]. Response status code is [{}]. But response body is null", URL_REQUEST, response.getStatusCode());
            throw new EmptyResponseBodyException();
        }
        log.info("Sku with id {} was updated.{}", id, body);
    }

    public void performDeleteByIdRequest(Long id) {
        restTemplate.delete(String.format(ID_URL_TEMPLATE, id));
    }

    public void performExportToCsv(HttpServletResponse servletResponse) throws IOException {
        List<SkuCreateDto> skus = performGetAll().stream().map(skuMapper::map).toList();
        servletResponse.setContentType("text/csv");
        servletResponse.addHeader("Content-Disposition", "attachment; filename=sku.csv");
        servletResponse.setCharacterEncoding("UTF-8");
        csvWriter.exportCsv(servletResponse.getWriter(), skus, SkuCreateDto.class);
    }

    public void performImportFromCsv(InputStream is) {
        List<SkuCreateDto> customers = csvReader.importCsv(is, SkuCreateDto.class);
        performSaveSkusRequest(customers);
    }
}
