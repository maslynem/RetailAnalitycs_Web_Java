package ru.school.retailanalitycs_web_java.controllers.entityControllers;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.school.retailanalitycs_web_java.dto.entityDto.cardDto.CardCreateDto;
import ru.school.retailanalitycs_web_java.dto.entityDto.cardDto.CardReadDto;
import ru.school.retailanalitycs_web_java.entities.tables.Card;
import ru.school.retailanalitycs_web_java.exceptions.notFoundExceptions.CardNotFoundException;
import ru.school.retailanalitycs_web_java.mapper.CardMapper;
import ru.school.retailanalitycs_web_java.services.entityServices.CardService;

import java.util.List;

@RestController
@RequestMapping("api/v1/cards")
public class CardController {
    private final CardService cardService;
    private final CardMapper cardMapper;

    public CardController(CardService cardService, CardMapper cardMapper) {
        this.cardService = cardService;
        this.cardMapper = cardMapper;
    }

    @GetMapping
    public List<CardReadDto> findAllCards() {
        return cardService.findAll().stream().map(cardMapper::toReadDto).toList();
    }

    @GetMapping(params = {"page", "size"})
    public Page<CardReadDto> findAllCardsByPage(@RequestParam("page") int page,
                                                @RequestParam("size") int size) {
        return cardService.findAllByPage(page, size).map(cardMapper::toReadDto);
    }

    @GetMapping("/{id}")
    public CardReadDto findCardById(@PathVariable Long id) {
        return cardService.findById(id).map(cardMapper::toReadDto).orElseThrow(() -> new CardNotFoundException(id));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public CardReadDto save(@Valid @RequestBody CardCreateDto cardDto) {
        Card card = cardMapper.toEntity(cardDto);
        Card save = cardService.save(card);
        return cardMapper.toReadDto(save);
    }

    @PostMapping(value = "all", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void saveAll(@Valid @RequestBody List<CardCreateDto> cardsDto) {
        List<Card> cards = cardsDto.stream().map(cardMapper::toEntity).toList();
        cardService.save(cards);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public CardReadDto update(@PathVariable Long id, @Valid @RequestBody CardCreateDto cardDto) {
        Card card = cardMapper.toEntity(cardDto);
        Card updated = cardService.update(id, card);
        return cardMapper.toReadDto(updated);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        cardService.deleteById(id);
    }

}
