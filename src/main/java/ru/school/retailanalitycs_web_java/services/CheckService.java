package ru.school.retailanalitycs_web_java.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.school.retailanalitycs_web_java.entities.tables.Check;
import ru.school.retailanalitycs_web_java.entities.tables.CheckId;
import ru.school.retailanalitycs_web_java.exceptions.duplicateValue.CheckDuplicateValueException;
import ru.school.retailanalitycs_web_java.repositories.CheckRepository;

@Service
public class CheckService extends BaseService<Check, CheckId> {
    @Autowired
    protected CheckService(CheckRepository repository) {
        super(repository);
    }

    @Override
    public Check save(Check entity) {
        CheckId checkId = entity.getId();
        repository.findById(checkId)
                .ifPresent(store -> {
                    throw new CheckDuplicateValueException(checkId);
                });
        return super.save(entity);
    }
}
