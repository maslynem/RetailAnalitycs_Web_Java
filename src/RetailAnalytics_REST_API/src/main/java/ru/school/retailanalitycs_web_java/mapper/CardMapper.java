package ru.school.retailanalitycs_web_java.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import ru.school.retailanalitycs_web_java.dto.entityDto.cardDto.CardCreateDto;
import ru.school.retailanalitycs_web_java.dto.entityDto.cardDto.CardReadDto;
import ru.school.retailanalitycs_web_java.entities.tables.Card;
import ru.school.retailanalitycs_web_java.exceptions.notFoundExceptions.CardNotFoundException;
import ru.school.retailanalitycs_web_java.services.entityServices.CardService;

@Mapper(componentModel = "spring", uses = {CustomerMapper.class})
public abstract class CardMapper {
    @Autowired
    private CardService cardService;

    public abstract CardReadDto toReadDto(Card entity);

    @Mapping(target = "customer", source = "customer.id")
    public abstract CardCreateDto toCreateDto(Card entity);



    @Mapping(target = "transactions", ignore = true)
    public abstract Card toEntity(CardCreateDto dto);

    public Card toDtoById(Long cardID) {
        return cardService.findById(cardID).orElseThrow(() -> new CardNotFoundException(cardID));
    }
}
