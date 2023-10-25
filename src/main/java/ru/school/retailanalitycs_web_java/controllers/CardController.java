package ru.school.retailanalitycs_web_java.controllers;

import jakarta.validation.Valid;
import lombok.SneakyThrows;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.school.retailanalitycs_web_java.dto.CardDto;
import ru.school.retailanalitycs_web_java.entities.tables.Card;
import ru.school.retailanalitycs_web_java.exceptions.notFoundExceptions.CardNotFoundException;
import ru.school.retailanalitycs_web_java.mapper.CardMapper;
import ru.school.retailanalitycs_web_java.services.CardService;
import ru.school.retailanalitycs_web_java.utils.CsvReader;

import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("api/v1/cards")
public class CardController {
    private final CardService cardService;
    private final CardMapper cardMapper;
    private final CsvReader<CardDto> csvReader;

    public CardController(CardService cardService, CardMapper cardMapper, CsvReader<CardDto> csvReader) {
        this.cardService = cardService;
        this.cardMapper = cardMapper;
        this.csvReader = csvReader;
    }

    @GetMapping
    public List<CardDto> findAllCustomers() {
        return cardService.findAll().stream().map(cardMapper::toDto).toList();
    }

    @GetMapping(params = {"page", "size"})
    public Page<CardDto> findAllCustomersByPage(@RequestParam("page") int page,
                                                @RequestParam("size") int size) {
        return cardService.findAllByPage(page, size).map(cardMapper::toDto);
    }

    @GetMapping("/{id}")
    public CardDto findCustomerById(@PathVariable Integer id) {
        return cardService.findById(id).map(cardMapper::toDto).orElseThrow(() -> new CardNotFoundException(id));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public CardDto create(@Valid @RequestBody CardDto customerDto) {
        Card customer = cardMapper.toEntity(customerDto);
        Card save = cardService.save(customer);
        customerDto.setId(save.getId());
        return customerDto;
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Integer id) {
        cardService.deleteById(id);
    }

    @PostMapping(value = "import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @SneakyThrows
    public void importFromCsv(@RequestPart MultipartFile multipartFile) {
        InputStream inputStream = multipartFile.getInputStream();
        List<Card> cards = csvReader.importCsv(inputStream, CardDto.class).stream().map(cardMapper::toEntity).toList();
        cardService.save(cards);
    }
}
