package ru.school.retailanalitycs_web_java.controller;

import lombok.SneakyThrows;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.school.retailanalitycs_web_java.dto.CardDto;
import ru.school.retailanalitycs_web_java.entities.tables.Card;
import ru.school.retailanalitycs_web_java.mapper.CardMapper;
import ru.school.retailanalitycs_web_java.services.CardService;
import ru.school.retailanalitycs_web_java.utils.CsvReader;

import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("api/v1/card")
public class CardController {
    private final CardService cardService;
    private final CardMapper cardMapper;
    private final CsvReader<CardDto> csvReader;

    public CardController(CardService cardService, CardMapper cardMapper, CsvReader<CardDto> csvReader) {
        this.cardService = cardService;
        this.cardMapper = cardMapper;
        this.csvReader = csvReader;
    }

    @GetMapping("import")
    @SneakyThrows
    public void importFromCsv(@RequestBody MultipartFile multipartFile) {
//        InputStream inputStream = new ClassPathResource("datasets/Cards_Mini.tsv").getInputStream();
        InputStream inputStream = multipartFile.getInputStream();
        List<Card> cards = csvReader.importCsv(inputStream, CardDto.class).stream().map(cardMapper::toEntity).toList();
        cardService.save(cards);
    }
}
