package ru.school.retailanalitycs_web_java.services.entityServices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.school.retailanalitycs_web_java.entities.tables.Card;
import ru.school.retailanalitycs_web_java.repositories.entityRepositories.CardRepository;

@Service
public class CardService extends BaseService<Card, Long> {

    @Autowired
    protected CardService(CardRepository repository) {
        super(repository);
    }
}
