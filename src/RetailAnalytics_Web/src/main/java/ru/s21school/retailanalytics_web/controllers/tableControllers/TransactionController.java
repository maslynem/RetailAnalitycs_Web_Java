package ru.s21school.retailanalytics_web.controllers.tableControllers;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import ru.s21school.retailanalytics_web.dto.ErrorDto;
import ru.s21school.retailanalytics_web.dto.entityDto.transactionDto.TransactionCreateDto;
import ru.s21school.retailanalytics_web.dto.entityDto.transactionDto.TransactionPageDto;
import ru.s21school.retailanalytics_web.dto.entityDto.transactionDto.TransactionReadDto;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Optional;

@Controller
@RequestMapping("data/transactions")
@Slf4j
public class TransactionController {
    private static final String TRANSACTION_API_URL = "http://localhost:8081/api/v1/transactions";

    private final RestTemplate restTemplate;
    private final ImportExportHandler importExportHandler;

    public TransactionController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.importExportHandler = new ImportExportHandler(TRANSACTION_API_URL, "transactions", restTemplate);
    }

    @GetMapping
    public String getTransactionsPage(@RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "30") int size,
                                      Model model) {
        ResponseEntity<TransactionPageDto> response =
                restTemplate.exchange(TRANSACTION_API_URL + String.format("?page=%d&size=%d", page, size),
                        HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                        });
        TransactionPageDto transactionsPage = Optional.ofNullable(response.getBody()).orElse(new TransactionPageDto());
        model.addAttribute("entities", transactionsPage.getContent());
        model.addAttribute("totalPages", transactionsPage.getTotalPages());
        model.addAttribute("totalElements", transactionsPage.getTotalElements());
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        return "tables/transactions/transactions";
    }

    @GetMapping("new")
    public String getCreateTransactionPage(Model model) {
        model.addAttribute("transaction", new TransactionReadDto());
        return "tables/transactions/new";
    }

    @PostMapping
    public String createTransaction(@Valid @ModelAttribute("transaction") TransactionCreateDto transaction, Model model) {
        try {
            HttpEntity<TransactionCreateDto> httpEntity = new HttpEntity<>(transaction);
            ResponseEntity<LinkedHashMap<String, Object>> response =
                    restTemplate.exchange(TRANSACTION_API_URL,
                            HttpMethod.POST, httpEntity, new ParameterizedTypeReference<>() {
                            });
            LinkedHashMap<String, Object> map = response.getBody();
            log.info("New transaction was saved. Id: {}", map.get("id"));
            return "redirect:/data/transactions";
        } catch (HttpClientErrorException exception) {
            ErrorDto errorDto = exception.getResponseBodyAs(ErrorDto.class);
            log.warn(errorDto.getMessages().toString());
            model.addAttribute("errors", errorDto.getMessages());
            return "tables/transactions/new";
        }
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable String id) {
        restTemplate.delete(TRANSACTION_API_URL + "/" + id);
        return "redirect:/data/transactions";
    }

    @GetMapping("/export")
    public void exportToCsv(HttpServletResponse servletResponse) throws IOException {
        importExportHandler.exportToCsv(servletResponse);
    }

    @PostMapping("/import")
    public String importFromCsv(@RequestParam MultipartFile file) {
        importExportHandler.importFromCsv(file);
        return "redirect:/data/transactions";
    }
}
