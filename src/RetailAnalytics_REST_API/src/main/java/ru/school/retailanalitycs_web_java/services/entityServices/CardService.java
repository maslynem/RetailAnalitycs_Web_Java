package ru.school.retailanalitycs_web_java.services.entityServices;

import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.school.retailanalitycs_web_java.entities.tables.Card;
import ru.school.retailanalitycs_web_java.exceptions.notFoundExceptions.CardNotFoundException;
import ru.school.retailanalitycs_web_java.repositories.entityRepositories.CardRepository;

@Service
public class CardService extends BaseService<Card, Long> {
    @Autowired
    protected CardService(CardRepository repository, EntityManager entityManager) {
        super(repository, entityManager);
    }

    @Override
    @Transactional
    public Card update(Long id, Card entity) {
        Card card = findById(id).orElseThrow(() -> new CardNotFoundException(id));
        entity.setId(card.getId());
        return merge(entity);
    }
}
