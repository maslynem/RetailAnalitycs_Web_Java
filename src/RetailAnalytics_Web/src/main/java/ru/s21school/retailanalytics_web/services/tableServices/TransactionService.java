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
import ru.s21school.retailanalytics_web.dto.entityDto.transactionDto.TransactionCreateDto;
import ru.s21school.retailanalytics_web.dto.entityDto.transactionDto.TransactionPageDto;
import ru.s21school.retailanalytics_web.dto.entityDto.transactionDto.TransactionReadDto;
import ru.s21school.retailanalytics_web.exceptions.EmptyResponseBodyException;

import java.io.IOException;

@Service
@Slf4j
public class TransactionService {
    private static final String TRANSACTION_API_URL = "http://localhost:8081/api/v1/transactions";
    private final ImportExportHandler importExportHandler;
    private final RestTemplate restTemplate;
    private final String PAGE_URL_TEMPLATE;
    private final String ID_URL_TEMPLATE;

    public TransactionService(RestTemplate restTemplate) {
        this.importExportHandler = new ImportExportHandler(TRANSACTION_API_URL, "transactions", restTemplate);
        this.restTemplate = restTemplate;
        PAGE_URL_TEMPLATE = TRANSACTION_API_URL + "?page=%d&size=%d";
        ID_URL_TEMPLATE = TRANSACTION_API_URL + "/%d";
    }

    public TransactionPageDto performGetPageRequest(int page, int size) {
        final String URL_REQUEST = String.format(PAGE_URL_TEMPLATE, page, size);
        ResponseEntity<TransactionPageDto> response =
                restTemplate.exchange(URL_REQUEST,
                        HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                        });
        TransactionPageDto body = response.getBody();
        if (body == null) {
            log.error("Error after request to [{}]. Response status code is [{}]. But response body is null", URL_REQUEST, response.getStatusCode());
            throw new EmptyResponseBodyException();
        }
        return body;
    }

    public TransactionReadDto performFindByIdRequest(Long id) {
        final String URL_REQUEST = String.format(ID_URL_TEMPLATE, id);
        ResponseEntity<TransactionReadDto> response =
                restTemplate.exchange(URL_REQUEST,
                        HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                        });
        TransactionReadDto body = response.getBody();
        if (body == null) {
            log.error("Error after request to [{}]. Response status code is [{}]. But response body is null", URL_REQUEST, response.getStatusCode());
            throw new EmptyResponseBodyException();
        }
        return body;
    }

    public void performSaveTransactionRequest(TransactionCreateDto transaction) {
        HttpEntity<TransactionCreateDto> httpEntity = new HttpEntity<>(transaction);
        ResponseEntity<TransactionReadDto> response =
                restTemplate.exchange(TRANSACTION_API_URL,
                        HttpMethod.POST, httpEntity, new ParameterizedTypeReference<>() {
                        });
        TransactionReadDto body = response.getBody();
        if (body == null) {
            log.error("Error after request to [{}]. Response status code is [{}]. But response body is null", TRANSACTION_API_URL, response.getStatusCode());
            throw new EmptyResponseBodyException();
        }
        log.info("New transaction was saved. Id: {}", body.getId());
    }

    public void performUpdateTransactionRequest(Long id, TransactionCreateDto transaction) {
        final String URL_REQUEST = String.format(ID_URL_TEMPLATE, id);
        HttpEntity<TransactionCreateDto> httpEntity = new HttpEntity<>(transaction);
        ResponseEntity<TransactionReadDto> response =
                restTemplate.exchange(URL_REQUEST,
                        HttpMethod.PUT, httpEntity, new ParameterizedTypeReference<>() {
                        });
        TransactionReadDto body = response.getBody();
        if (body == null) {
            log.error("Error after request to [{}]. Response status code is [{}]. But response body is null", URL_REQUEST, response.getStatusCode());
            throw new EmptyResponseBodyException();
        }
        log.info("Transaction with id {} was updated.{}", id, body);
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
