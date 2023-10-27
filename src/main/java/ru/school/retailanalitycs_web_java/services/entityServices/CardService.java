package ru.school.retailanalitycs_web_java.services.entityServices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import ru.school.retailanalitycs_web_java.entities.tables.Card;

@Service
public class CardService extends BaseService<Card, Integer> {

    @Autowired
    protected CardService(JpaRepository<Card, Integer> repository) {
        super(repository);
    }
}
