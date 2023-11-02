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
import ru.s21school.retailanalytics_web.dto.entityDto.cardDto.CardCreateDto;
import ru.s21school.retailanalytics_web.dto.entityDto.cardDto.CardReadDto;
import ru.s21school.retailanalytics_web.exceptions.EmptyResponseBodyException;
import ru.s21school.retailanalytics_web.mappers.CardMapper;
import ru.s21school.retailanalytics_web.utils.CsvReader;
import ru.s21school.retailanalytics_web.utils.CsvWriter;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
@Slf4j
public class CardService {
    private static final String CARD_API_URL = "http://retailAnalyticsApi/api/v1/cards";
    private final RestTemplate restTemplate;
    private final String PAGE_URL_TEMPLATE;
    private final String ID_URL_TEMPLATE;
    private final CardMapper cardMapper;
    private final CsvReader<CardCreateDto> csvReader;
    private final CsvWriter<CardCreateDto> csvWriter;

    public CardService(RestTemplate restTemplate, CardMapper cardMapper, CsvReader<CardCreateDto> csvReader, CsvWriter<CardCreateDto> csvWriter) {
        this.restTemplate = restTemplate;
        this.cardMapper = cardMapper;
        this.csvReader = csvReader;
        this.csvWriter = csvWriter;
        PAGE_URL_TEMPLATE = CARD_API_URL + "?page=%d&size=%d";
        ID_URL_TEMPLATE = CARD_API_URL + "/%d";
    }

    public List<CardReadDto> performGetAll() {
        ResponseEntity<List<CardReadDto>> response =
                restTemplate.exchange(CARD_API_URL,
                        HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                        });
        List<CardReadDto> body = response.getBody();
        if (body == null) {
            log.error("Error after request to [{}]. Response status code is [{}]. But response body is null", CARD_API_URL, response.getStatusCode());
            throw new EmptyResponseBodyException();
        }
        return body;
    }

    public PageDto<CardReadDto> performGetPageRequest(int page, int size) {
        final String URL_REQUEST = String.format(PAGE_URL_TEMPLATE, page, size);
        ResponseEntity<PageDto<CardReadDto>> response =
                restTemplate.exchange(URL_REQUEST,
                        HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                        });
        PageDto<CardReadDto> body = response.getBody();
        if (body == null) {
            log.error("Error after request to [{}]. Response status code is [{}]. But response body is null", URL_REQUEST, response.getStatusCode());
            throw new EmptyResponseBodyException();
        }
        return body;
    }

    public CardReadDto performFindByIdRequest(Long id) {
        final String URL_REQUEST = String.format(ID_URL_TEMPLATE, id);
        ResponseEntity<CardReadDto> response =
                restTemplate.exchange(URL_REQUEST,
                        HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                        });
        CardReadDto body = response.getBody();
        if (body == null) {
            log.error("Error after request to [{}]. Response status code is [{}]. But response body is null", URL_REQUEST, response.getStatusCode());
            throw new EmptyResponseBodyException();
        }
        return body;
    }

    public void performSaveCardRequest(CardCreateDto card) {
        HttpEntity<CardCreateDto> httpEntity = new HttpEntity<>(card);
        ResponseEntity<CardReadDto> response =
                restTemplate.exchange(CARD_API_URL,
                        HttpMethod.POST, httpEntity, new ParameterizedTypeReference<>() {
                        });
        CardReadDto body = response.getBody();
        if (body == null) {
            log.error("Error after request to [{}]. Response status code is [{}]. But response body is null", CARD_API_URL, response.getStatusCode());
            throw new EmptyResponseBodyException();
        }
        log.info("New card was saved. Id: {}", body.getId());
    }

    public void performSaveCardsRequest(List<CardCreateDto> cards) {
        HttpEntity<List<CardCreateDto>> httpEntity = new HttpEntity<>(cards);
        restTemplate.exchange(CARD_API_URL + "/all",
                HttpMethod.POST, httpEntity, new ParameterizedTypeReference<>() {
                });
        log.info("Cards was saved. Size: {}", cards.size());
    }

    public void performUpdateCardRequest(Long id, CardCreateDto card) {
        final String URL_REQUEST = String.format(ID_URL_TEMPLATE, id);
        HttpEntity<CardCreateDto> httpEntity = new HttpEntity<>(card);
        ResponseEntity<CardReadDto> response =
                restTemplate.exchange(URL_REQUEST,
                        HttpMethod.PUT, httpEntity, new ParameterizedTypeReference<>() {
                        });
        CardReadDto body = response.getBody();
        if (body == null) {
            log.error("Error after request to [{}]. Response status code is [{}]. But response body is null", URL_REQUEST, response.getStatusCode());
            throw new EmptyResponseBodyException();
        }
        log.info("Card with id {} was updated.{}", id, body);
    }

    public void performDeleteByIdRequest(Long id) {
        restTemplate.delete(String.format(ID_URL_TEMPLATE, id));
    }

    public void performExportToCsv(HttpServletResponse servletResponse) throws IOException {
        List<CardCreateDto> cards = performGetAll().stream().map(cardMapper::map).toList();
        servletResponse.setContentType("text/csv");
        servletResponse.addHeader("Content-Disposition", "attachment; filename=cards.csv");
        servletResponse.setCharacterEncoding("UTF-8");
        csvWriter.exportCsv(servletResponse.getWriter(), cards, CardCreateDto.class);
    }

    public void performImportFromCsv(InputStream is) {
        List<CardCreateDto> customers = csvReader.importCsv(is, CardCreateDto.class);
        performSaveCardsRequest(customers);
    }
}
