package ru.school.retailanalitycs_web_java.controllers.entityControllers;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.school.retailanalitycs_web_java.dto.entityDto.transactionDto.TransactionCreateDto;
import ru.school.retailanalitycs_web_java.dto.entityDto.transactionDto.TransactionReadDto;
import ru.school.retailanalitycs_web_java.entities.tables.Transaction;
import ru.school.retailanalitycs_web_java.exceptions.notFoundExceptions.TransactionNotFoundException;
import ru.school.retailanalitycs_web_java.mapper.TransactionMapper;
import ru.school.retailanalitycs_web_java.services.entityServices.TransactionService;

import java.util.List;

@RestController
@RequestMapping("api/v1/transactions")
public class TransactionController {
    private final TransactionService transactionService;
    private final TransactionMapper transactionMapper;

    public TransactionController(TransactionService transactionService, TransactionMapper transactionMapper) {
        this.transactionService = transactionService;
        this.transactionMapper = transactionMapper;
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
    public TransactionReadDto findTransactionById(@PathVariable Long id) {
        return transactionService.findById(id).map(transactionMapper::toDto).orElseThrow(() -> new TransactionNotFoundException(id));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public TransactionReadDto save(@Valid @RequestBody TransactionCreateDto transactionDto) {
        Transaction transaction = transactionMapper.toEntity(transactionDto);
        Transaction save = transactionService.save(transaction);
        return transactionMapper.toDto(save);
    }

    @PostMapping(value = "all", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void saveAll(@Valid @RequestBody List<TransactionCreateDto> transactionDto) {
        List<Transaction> transactions = transactionDto.stream().map(transactionMapper::toEntity).toList();
        transactionService.save(transactions);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public TransactionReadDto update(@PathVariable Long id, @Valid @RequestBody TransactionCreateDto dto) {
        Transaction entity = transactionMapper.toEntity(dto);
        Transaction updated = transactionService.update(id, entity);
        return transactionMapper.toDto(updated);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        transactionService.deleteById(id);
    }

}
