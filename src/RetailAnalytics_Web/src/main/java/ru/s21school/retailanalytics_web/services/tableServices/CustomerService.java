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
import ru.s21school.retailanalytics_web.dto.entityDto.customerDto.CustomerDto;
import ru.s21school.retailanalytics_web.exceptions.EmptyResponseBodyException;
import ru.s21school.retailanalytics_web.utils.CsvReader;
import ru.s21school.retailanalytics_web.utils.CsvWriter;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
@Slf4j
public class CustomerService {
    private static final String CUSTOMER_API_URL = "http://retailAnalyticsApi/api/v1/customers";
    private final RestTemplate restTemplate;
    private final String PAGE_URL_TEMPLATE;
    private final String ID_URL_TEMPLATE;
    private final CsvReader<CustomerDto> csvReader;
    private final CsvWriter<CustomerDto> csvWriter;

    public CustomerService(RestTemplate restTemplate, CsvReader<CustomerDto> csvReader, CsvWriter<CustomerDto> csvWriter) {
        this.restTemplate = restTemplate;
        this.csvReader = csvReader;
        this.csvWriter = csvWriter;
        PAGE_URL_TEMPLATE = CUSTOMER_API_URL + "?page=%d&size=%d";
        ID_URL_TEMPLATE = CUSTOMER_API_URL + "/%d";
    }

    public List<CustomerDto> performGetAll() {
        ResponseEntity<List<CustomerDto>> response =
                restTemplate.exchange(CUSTOMER_API_URL,
                        HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                        });
        List<CustomerDto> body = response.getBody();
        if (body == null) {
            log.error("Error after request to [{}]. Response status code is [{}]. But response body is null", CUSTOMER_API_URL, response.getStatusCode());
            throw new EmptyResponseBodyException();
        }
        return body;
    }

    public PageDto<CustomerDto> performGetPageRequest(int page, int size) {
        final String URL_REQUEST = String.format(PAGE_URL_TEMPLATE, page, size);
        ResponseEntity<PageDto<CustomerDto>> response =
                restTemplate.exchange(URL_REQUEST,
                        HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                        });
        PageDto<CustomerDto> body = response.getBody();
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

    public void performSaveCustomersRequest(List<CustomerDto> customers) {
        HttpEntity<List<CustomerDto>> httpEntity = new HttpEntity<>(customers);
        restTemplate.exchange(CUSTOMER_API_URL + "/all",
                HttpMethod.POST, httpEntity, new ParameterizedTypeReference<>() {
                });
        log.info("Customers was saved. Size: {}", customers.size());
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
        List<CustomerDto> customers = performGetAll();
        servletResponse.setContentType("text/csv");
        servletResponse.addHeader("Content-Disposition", "attachment; filename=customers.csv");
        servletResponse.setCharacterEncoding("UTF-8");
        csvWriter.exportCsv(servletResponse.getWriter(), customers, CustomerDto.class);
    }

    public void performImportFromCsv(InputStream is) {
        List<CustomerDto> customers = csvReader.importCsv(is, CustomerDto.class);
        performSaveCustomersRequest(customers);
    }
}
