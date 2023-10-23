package ru.school.retailanalitycs_web_java.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import ru.school.retailanalitycs_web_java.dto.CardDto;
import ru.school.retailanalitycs_web_java.entities.tables.Card;
import ru.school.retailanalitycs_web_java.utils.CsvReader;
import ru.school.retailanalitycs_web_java.utils.CsvWriter;


@Service
public class CardService extends BaseService<Card, Integer> {
    private final CsvWriter<CardDto> csvWriter;
    private final CsvReader<CardDto> csvReader;

    @Autowired
    protected CardService(JpaRepository<Card, Integer> repository, CsvWriter<CardDto> csvWriter, CsvReader<CardDto> csvReader) {
        super(repository);
        this.csvWriter = csvWriter;
        this.csvReader = csvReader;
    }
}
