package ru.s21school.retailanalytics_web.services.tableServices;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.s21school.retailanalytics_web.dto.entityDto.skuGroupDto.SkuGroupDto;
import ru.s21school.retailanalytics_web.dto.entityDto.skuGroupDto.SkuGroupPageDto;
import ru.s21school.retailanalytics_web.exceptions.EmptyResponseBodyException;
import ru.s21school.retailanalytics_web.utils.CsvReader;
import ru.s21school.retailanalytics_web.utils.CsvWriter;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
@Slf4j
public class SkuGroupService {
    private static final String SKU_GROUP_API_URL = "http://localhost:8081/api/v1/sku-groups";
    private final RestTemplate restTemplate;
    private final String PAGE_URL_TEMPLATE;
    private final String ID_URL_TEMPLATE;
    private final CsvReader<SkuGroupDto> csvReader;
    private final CsvWriter<SkuGroupDto> csvWriter;

    public SkuGroupService(RestTemplate restTemplate, CsvReader<SkuGroupDto> csvReader, CsvWriter<SkuGroupDto> csvWriter) {
        this.restTemplate = restTemplate;
        this.csvReader = csvReader;
        this.csvWriter = csvWriter;
        PAGE_URL_TEMPLATE = SKU_GROUP_API_URL + "?page=%d&size=%d";
        ID_URL_TEMPLATE = SKU_GROUP_API_URL + "/%d";
    }

    public List<SkuGroupDto> performGetAll() {
        ResponseEntity<List<SkuGroupDto>> response =
                restTemplate.exchange(SKU_GROUP_API_URL,
                        HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                        });
        List<SkuGroupDto> body = response.getBody();
        if (body == null) {
            log.error("Error after request to [{}]. Response status code is [{}]. But response body is null", SKU_GROUP_API_URL, response.getStatusCode());
            throw new EmptyResponseBodyException();
        }
        return body;
    }
    
    public SkuGroupPageDto performGetPageRequest(int page, int size) {
        final String URL_REQUEST = String.format(PAGE_URL_TEMPLATE, page, size);
        ResponseEntity<SkuGroupPageDto> response =
                restTemplate.exchange(URL_REQUEST,
                        HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                        });
        SkuGroupPageDto body = response.getBody();
        if (body == null) {
            log.error("Error after request to [{}]. Response status code is [{}]. But response body is null", URL_REQUEST, response.getStatusCode());
            throw new EmptyResponseBodyException();
        }
        return body;
    }

    public SkuGroupDto performFindByIdRequest(Long id) {
        final String URL_REQUEST = String.format(ID_URL_TEMPLATE, id);
        ResponseEntity<SkuGroupDto> response =
                restTemplate.exchange(URL_REQUEST,
                        HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                        });
        SkuGroupDto body = response.getBody();
        if (body == null) {
            log.error("Error after request to [{}]. Response status code is [{}]. But response body is null", URL_REQUEST, response.getStatusCode());
            throw new EmptyResponseBodyException();
        }
        return body;
    }

    public void performSaveSkuGroupRequest(SkuGroupDto skuGroup) {
        HttpEntity<SkuGroupDto> httpEntity = new HttpEntity<>(skuGroup);
        ResponseEntity<SkuGroupDto> response =
                restTemplate.exchange(SKU_GROUP_API_URL,
                        HttpMethod.POST, httpEntity, new ParameterizedTypeReference<>() {
                        });
        SkuGroupDto body = response.getBody();
        if (body == null) {
            log.error("Error after request to [{}]. Response status code is [{}]. But response body is null", SKU_GROUP_API_URL, response.getStatusCode());
            throw new EmptyResponseBodyException();
        }
        log.info("New skuGroup was saved. Id: {}", body.getId());
    }

    public void performSaveSkuGroupsRequest(List<SkuGroupDto> skuGroups) {
        HttpEntity<List<SkuGroupDto>> httpEntity = new HttpEntity<>(skuGroups);
        restTemplate.exchange(SKU_GROUP_API_URL + "/all",
                HttpMethod.POST, httpEntity, new ParameterizedTypeReference<>() {
                });
        log.info("SkuGroups was saved. Size: {}", skuGroups.size());
    }
    
    public void performUpdateSkuGroupRequest(Long id, SkuGroupDto skuGroup) {
        final String URL_REQUEST = String.format(ID_URL_TEMPLATE, id);
        HttpEntity<SkuGroupDto> httpEntity = new HttpEntity<>(skuGroup);
        ResponseEntity<SkuGroupDto> response =
                restTemplate.exchange(URL_REQUEST,
                        HttpMethod.PUT, httpEntity, new ParameterizedTypeReference<>() {
                        });
        SkuGroupDto body = response.getBody();
        if (body == null) {
            log.error("Error after request to [{}]. Response status code is [{}]. But response body is null", URL_REQUEST, response.getStatusCode());
            throw new EmptyResponseBodyException();
        }
        log.info("SkuGroup with id {} was updated.{}", id, body);
    }

    public void performDeleteByIdRequest(Long id) {
        restTemplate.delete(String.format(ID_URL_TEMPLATE, id));
    }

    public void performExportToCsv(HttpServletResponse servletResponse) throws IOException {
        List<SkuGroupDto> skuGroups = performGetAll();
        servletResponse.setContentType("text/csv");
        servletResponse.addHeader("Content-Disposition", "attachment; filename=skuGroups.csv");
        servletResponse.setCharacterEncoding("UTF-8");
        csvWriter.exportCsv(servletResponse.getWriter(), skuGroups, SkuGroupDto.class);
    }

    public void performImportFromCsv(InputStream is) {
        List<SkuGroupDto> skuGroups = csvReader.importCsv(is, SkuGroupDto.class);
        performSaveSkuGroupsRequest(skuGroups);
    }
}
