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
import ru.s21school.retailanalytics_web.dto.entityDto.customerDto.CustomerDto;
import ru.s21school.retailanalytics_web.dto.entityDto.customerDto.CustomerPageDto;
import ru.s21school.retailanalytics_web.exceptions.EmptyResponseBodyException;

import java.io.IOException;

@Service
@Slf4j
public class CustomerService {
    private static final String CUSTOMER_API_URL = "http://localhost:8081/api/v1/customers";
    private final ImportExportHandler importExportHandler;
    private final RestTemplate restTemplate;
    private final String PAGE_URL_TEMPLATE;
    private final String ID_URL_TEMPLATE;

    public CustomerService(RestTemplate restTemplate) {
        this.importExportHandler = new ImportExportHandler(CUSTOMER_API_URL, "customers", restTemplate);
        this.restTemplate = restTemplate;
        PAGE_URL_TEMPLATE = CUSTOMER_API_URL + "?page=%d&size=%d";
        ID_URL_TEMPLATE = CUSTOMER_API_URL + "/%d";
    }

    public CustomerPageDto performGetPageRequest(int page, int size) {
        final String URL_REQUEST = String.format(PAGE_URL_TEMPLATE, page, size);
        ResponseEntity<CustomerPageDto> response =
                restTemplate.exchange(URL_REQUEST,
                        HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                        });
        CustomerPageDto body = response.getBody();
        if (body == null) {
            log.error("Error after request to [{}]. Response status code is [{}]. But response body is null", URL_REQUEST, response.getStatusCode());
            throw new EmptyResponseBodyException();
        }
        return body;
    }

    public CustomerDto performFindByIdRequest(Long id) {
        final String URL_REQUEST = String.format(ID_URL_TEMPLATE, id);
        ResponseEntity<CustomerDto> response =
                restTemplate.exchange(URL_REQUEST,
                        HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                        });
        CustomerDto body = response.getBody();
        if (body == null) {
            log.error("Error after request to [{}]. Response status code is [{}]. But response body is null", URL_REQUEST, response.getStatusCode());
            throw new EmptyResponseBodyException();
        }
        return body;
    }

    public void performSaveCustomerRequest(CustomerDto customer) {
        HttpEntity<CustomerDto> httpEntity = new HttpEntity<>(customer);
        ResponseEntity<CustomerDto> response =
                restTemplate.exchange(CUSTOMER_API_URL,
                        HttpMethod.POST, httpEntity, new ParameterizedTypeReference<>() {
                        });
        CustomerDto body = response.getBody();
        if (body == null) {
            log.error("Error after request to [{}]. Response status code is [{}]. But response body is null", CUSTOMER_API_URL, response.getStatusCode());
            throw new EmptyResponseBodyException();
        }
        log.info("New customer was saved. Id: {}", body.getId());
    }

    public void performUpdateCustomerRequest(Long id, CustomerDto customer) {
        final String URL_REQUEST = String.format(ID_URL_TEMPLATE, id);
        HttpEntity<CustomerDto> httpEntity = new HttpEntity<>(customer);
        ResponseEntity<CustomerDto> response =
                restTemplate.exchange(URL_REQUEST,
                        HttpMethod.PUT, httpEntity, new ParameterizedTypeReference<>() {
                        });
        CustomerDto body = response.getBody();
        if (body == null) {
            log.error("Error after request to [{}]. Response status code is [{}]. But response body is null", URL_REQUEST, response.getStatusCode());
            throw new EmptyResponseBodyException();
        }
        log.info("Customer with id {} was updated.{}", id, body);
    }

    public void performDeleteByIdRequest(Long id) {
        restTemplate.delete(String.format(ID_URL_TEMPLATE, id));
    }

    public void performExportToCsv(HttpServletResponse servletResponse) throws IOException {
        importExportHandler.exportToCsv(servletResponse);
    }

    public void performImportFromCsv(MultipartFile file) {
        importExportHandler.importFromCsv(file);
    }
}
