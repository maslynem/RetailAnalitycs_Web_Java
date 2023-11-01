package ru.school.retailanalitycs_web_java.services.entityServices;

import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.school.retailanalitycs_web_java.entities.tables.Check;
import ru.school.retailanalitycs_web_java.entities.tables.CheckId;
import ru.school.retailanalitycs_web_java.exceptions.duplicateValue.CheckDuplicateValueException;
import ru.school.retailanalitycs_web_java.exceptions.notFoundExceptions.CheckNotFoundException;
import ru.school.retailanalitycs_web_java.repositories.entityRepositories.CheckRepository;

@Service
public class CheckService extends BaseService<Check, CheckId> {
    @Autowired
    protected CheckService(CheckRepository repository, EntityManager entityManager) {
        super(repository, entityManager);
    }

    @Override
    @Transactional
    public Check save(Check entity) {
        CheckId checkId = entity.getId();
        findById(checkId)
                .ifPresent(store -> {
                    throw new CheckDuplicateValueException(checkId);
                });
        return super.save(entity);
    }

    @Override
    @Transactional
    public Check update(CheckId id, Check entity) {
        Check check = findById(id).orElseThrow(() -> new CheckNotFoundException(id));
        entity.setId(check.getId());
        return merge(entity);
    }

}
