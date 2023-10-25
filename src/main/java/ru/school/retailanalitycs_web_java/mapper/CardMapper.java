package ru.school.retailanalitycs_web_java.mapper;

import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import ru.school.retailanalitycs_web_java.dto.CardDto;
import ru.school.retailanalitycs_web_java.entities.tables.Card;
import ru.school.retailanalitycs_web_java.exceptions.CardNotFoundException;
import ru.school.retailanalitycs_web_java.services.CardService;

@Mapper(componentModel = "spring", uses = {CustomerMapper.class})
public abstract class CardMapper extends GenericMapper<Card, CardDto> {

    @Autowired
    private CardService cardService;

    public Integer map(Card card) {
        return card.getId();
    }

    public Card map(Integer id) {
        return cardService.findById(id).orElseThrow(() -> new CardNotFoundException(id));
    }

}