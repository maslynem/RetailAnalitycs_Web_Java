package ru.school.retailanalitycs_web_java.controllers;

import jakarta.validation.Valid;
import lombok.SneakyThrows;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.school.retailanalitycs_web_java.dto.transactionDto.TransactionCreateDto;
import ru.school.retailanalitycs_web_java.dto.transactionDto.TransactionReadDto;
import ru.school.retailanalitycs_web_java.entities.tables.Transaction;
import ru.school.retailanalitycs_web_java.exceptions.notFoundExceptions.TransactionNotFoundException;
import ru.school.retailanalitycs_web_java.mapper.TransactionMapper;
import ru.school.retailanalitycs_web_java.services.TransactionService;
import ru.school.retailanalitycs_web_java.utils.CsvReader;

import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("api/v1/transactions")
public class TransactionController {
    private final TransactionService transactionService;
    private final TransactionMapper transactionMapper;
    private final CsvReader<TransactionCreateDto> csvReader;

    public TransactionController(TransactionService transactionService, TransactionMapper transactionMapper, CsvReader<TransactionCreateDto> csvReader) {
        this.transactionService = transactionService;
        this.transactionMapper = transactionMapper;
        this.csvReader = csvReader;
    }

    @GetMapping
    public List<TransactionReadDto> findAllTransactions() {
        return transactionService.findAll().stream().map(transactionMapper::toDto).toList();
    }

    @GetMapping(params = {"page", "size"})
    public Page<TransactionReadDto> findAlltransactionsByPage(@RequestParam("page") int page,
                                                          @RequestParam("size") int size) {
        return transactionService.findAllByPage(page, size).map(transactionMapper::toDto);
    }

    @GetMapping("/{id}")
    public TransactionReadDto findtransactionById(@PathVariable Integer id) {
        return transactionService.findById(id).map(transactionMapper::toDto).orElseThrow(() -> new TransactionNotFoundException(id));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public TransactionReadDto create(@Valid @RequestBody TransactionCreateDto transactionDto) {
        Transaction transaction = transactionMapper.toEntity(transactionDto);
        Transaction save = transactionService.save(transaction);
        return transactionMapper.toDto(save);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Integer id) {
        transactionService.deleteById(id);
    }

    @PostMapping(value = "import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @SneakyThrows
    public void importFromCsv(@RequestPart MultipartFile file) {
        InputStream inputStream = file.getInputStream();
        List<Transaction> cards = csvReader.importCsv(inputStream, TransactionCreateDto.class).stream().map(transactionMapper::toEntity).toList();
        transactionService.save(cards);
    }
}
