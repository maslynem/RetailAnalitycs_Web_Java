package ru.s21school.retailanalytics_web.controllers.tableControllers;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;
import ru.s21school.retailanalytics_web.dto.ErrorDto;
import ru.s21school.retailanalytics_web.dto.PageDto;
import ru.s21school.retailanalytics_web.dto.entityDto.transactionDto.TransactionCreateDto;
import ru.s21school.retailanalytics_web.dto.entityDto.transactionDto.TransactionReadDto;
import ru.s21school.retailanalytics_web.mappers.TransactionMapper;
import ru.s21school.retailanalytics_web.services.tableServices.TransactionService;

import java.io.IOException;
import java.util.Collections;

@Controller
@RequestMapping("data/transactions")
@Slf4j
public class TransactionController {
    private final TransactionService transactionService;
    private final TransactionMapper mapper;

    public TransactionController(TransactionService transactionService, TransactionMapper mapper) {
        this.transactionService = transactionService;
        this.mapper = mapper;
    }

    @GetMapping
    public String getTransactionsPage(@RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "30") int size,
                                      Model model) {
        PageDto<TransactionReadDto> transactionPage = transactionService.performGetPageRequest(page, size);
        model.addAttribute("entities", transactionPage.getContent());
        model.addAttribute("totalPages", transactionPage.getTotalPages());
        model.addAttribute("totalElements", transactionPage.getTotalElements());
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        return "tables/transactions/transactions";
    }

    @GetMapping("new")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String getCreateTransactionPage(Model model) {
        model.addAttribute("transaction", new TransactionCreateDto());
        return "tables/transactions/new";
    }

    @GetMapping("{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String getUpdateTransactionPage(@PathVariable Long id, Model model) {
        TransactionReadDto transactionReadDto = transactionService.performFindByIdRequest(id);
        model.addAttribute("transaction", mapper.map(transactionReadDto));
        return "tables/transactions/update";
    }

    @PutMapping("{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String updateTransaction(@PathVariable Long id,
                                    @ModelAttribute("transaction") TransactionCreateDto transaction,
                                    Model model) {
        try {
            transactionService.performUpdateTransactionRequest(id, transaction);
            return "redirect:/data/transactions";
        } catch (HttpClientErrorException exception) {
            ErrorDto errorDto = exception.getResponseBodyAs(ErrorDto.class);
            log.warn(errorDto.getMessages().toString());
            model.addAttribute("errors", errorDto.getMessages());
            return "tables/transactions/update";
        }
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public String createTransaction(@Valid @ModelAttribute("transaction") TransactionCreateDto transaction, Model model) {
        try {
            transactionService.performSaveTransactionRequest(transaction);
            return "redirect:/data/transactions";
        } catch (HttpClientErrorException exception) {
            ErrorDto errorDto = exception.getResponseBodyAs(ErrorDto.class);
            log.warn(errorDto.getMessages().toString());
            model.addAttribute("errors", errorDto.getMessages());
            return "tables/transactions/new";
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String delete(@PathVariable Long id) {
        transactionService.performDeleteByIdRequest(id);
        return "redirect:/data/transactions";
    }

    @GetMapping("/export")
    public void exportToCsv(HttpServletResponse servletResponse) throws IOException {
        transactionService.performExportToCsv(servletResponse);
    }

    @PostMapping("/import")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String importFromCsv(@RequestParam MultipartFile file, Model model) {
        try {
            transactionService.performImportFromCsv(file.getInputStream());
            return "redirect:/data/transactions";
        } catch (IOException e) {
            log.warn("IOException when importFromCsv called");
            model.addAttribute("errors", Collections.singletonList("Error during import. Try again"));
        } catch (HttpClientErrorException exception) {
            ErrorDto errorDto = exception.getResponseBodyAs(ErrorDto.class);
            log.warn(errorDto.getMessages().toString());
            model.addAttribute("errors", errorDto.getMessages());
        }
        return "tables/transactions/transactions";
    }
}
