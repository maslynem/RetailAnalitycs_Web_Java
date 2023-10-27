package ru.school.retailanalitycs_web_java.controllers.entityControllers;

import jakarta.validation.Valid;
import lombok.SneakyThrows;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.school.retailanalitycs_web_java.dto.entityDto.cardDto.CardCreateDto;
import ru.school.retailanalitycs_web_java.dto.entityDto.cardDto.CardReadDto;
import ru.school.retailanalitycs_web_java.entities.tables.Card;
import ru.school.retailanalitycs_web_java.exceptions.notFoundExceptions.CardNotFoundException;
import ru.school.retailanalitycs_web_java.mapper.CardMapper;
import ru.school.retailanalitycs_web_java.services.entityServices.CardService;
import ru.school.retailanalitycs_web_java.utils.CsvReader;

import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("api/v1/cards")
public class CardController {
    private final CardService cardService;
    private final CardMapper cardMapper;
    private final CsvReader<CardCreateDto> csvReader;

    public CardController(CardService cardService, CardMapper cardMapper, CsvReader<CardCreateDto> csvReader) {
        this.cardService = cardService;
        this.cardMapper = cardMapper;
        this.csvReader = csvReader;
    }

    @GetMapping
    public List<CardReadDto> findAllCards() {
        return cardService.findAll().stream().map(cardMapper::toDto).toList();
    }

    @GetMapping(params = {"page", "size"})
    public Page<CardReadDto> findAllCardsByPage(@RequestParam("page") int page,
                                                @RequestParam("size") int size) {
        return cardService.findAllByPage(page, size).map(cardMapper::toDto);
    }

    @GetMapping("/{id}")
    public CardReadDto findCardById(@PathVariable Long id) {
        return cardService.findById(id).map(cardMapper::toDto).orElseThrow(() -> new CardNotFoundException(id));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public CardReadDto create(@Valid @RequestBody CardCreateDto cardDto) {
        Card card = cardMapper.toEntity(cardDto);
        Card save = cardService.save(card);
        return cardMapper.toDto(save);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        cardService.deleteById(id);
    }

    @PostMapping(value = "import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @SneakyThrows
    public void importFromCsv(@RequestPart MultipartFile file) {
        InputStream inputStream = file.getInputStream();
        List<Card> cards = csvReader.importCsv(inputStream, CardCreateDto.class).stream().map(cardMapper::toEntity).toList();
        cardService.save(cards);
    }
}
