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
import ru.s21school.retailanalytics_web.dto.entityDto.transactionDto.TransactionCreateDto;
import ru.s21school.retailanalytics_web.dto.entityDto.transactionDto.TransactionReadDto;
import ru.s21school.retailanalytics_web.exceptions.EmptyResponseBodyException;
import ru.s21school.retailanalytics_web.mappers.TransactionMapper;
import ru.s21school.retailanalytics_web.utils.CsvReader;
import ru.s21school.retailanalytics_web.utils.CsvWriter;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
@Slf4j
public class TransactionService {
    private static final String TRANSACTION_API_URL = "http://retailAnalyticsApi/api/v1/transactions";
    private final RestTemplate restTemplate;
    private final String PAGE_URL_TEMPLATE;
    private final String ID_URL_TEMPLATE;
    private final TransactionMapper transactionMapper;
    private final CsvReader<TransactionCreateDto> csvReader;
    private final CsvWriter<TransactionCreateDto> csvWriter;

    public TransactionService(RestTemplate restTemplate, TransactionMapper transactionMapper, CsvReader<TransactionCreateDto> csvReader, CsvWriter<TransactionCreateDto> csvWriter) {
        this.restTemplate = restTemplate;
        this.transactionMapper = transactionMapper;
        this.csvReader = csvReader;
        this.csvWriter = csvWriter;
        PAGE_URL_TEMPLATE = TRANSACTION_API_URL + "?page=%d&size=%d";
        ID_URL_TEMPLATE = TRANSACTION_API_URL + "/%d";
    }

    public List<TransactionReadDto> performGetAll() {
        ResponseEntity<List<TransactionReadDto>> response =
                restTemplate.exchange(TRANSACTION_API_URL,
                        HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                        });
        List<TransactionReadDto> body = response.getBody();
        if (body == null) {
            log.error("Error after request to [{}]. Response status code is [{}]. But response body is null", TRANSACTION_API_URL, response.getStatusCode());
            throw new EmptyResponseBodyException();
        }
        return body;
    }


    public PageDto<TransactionReadDto> performGetPageRequest(int page, int size) {
        final String URL_REQUEST = String.format(PAGE_URL_TEMPLATE, page, size);
        ResponseEntity<PageDto<TransactionReadDto>> response =
                restTemplate.exchange(URL_REQUEST,
                        HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                        });
        PageDto<TransactionReadDto> body = response.getBody();
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

    public void performSaveTransactionsRequest(List<TransactionCreateDto> transactions) {
        HttpEntity<List<TransactionCreateDto>> httpEntity = new HttpEntity<>(transactions);
        restTemplate.exchange(TRANSACTION_API_URL + "/all",
                HttpMethod.POST, httpEntity, new ParameterizedTypeReference<>() {
                });
        log.info("Transactions was saved. Size: {}", transactions.size());
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
        List<TransactionCreateDto> transactions = performGetAll().stream().map(transactionMapper::map).toList();
        servletResponse.setContentType("text/csv");
        servletResponse.addHeader("Content-Disposition", "attachment; filename=transactions.csv");
        servletResponse.setCharacterEncoding("UTF-8");
        csvWriter.exportCsv(servletResponse.getWriter(), transactions, TransactionCreateDto.class);
    }

    public void performImportFromCsv(InputStream is) {
        List<TransactionCreateDto> customers = csvReader.importCsv(is, TransactionCreateDto.class);
        performSaveTransactionsRequest(customers);
    }
}
