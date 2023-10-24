package ru.school.retailanalitycs_web_java.services;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.school.retailanalitycs_web_java.entities.tables.Check;
import ru.school.retailanalitycs_web_java.entities.tables.CheckId;

public class CheckService extends BaseService<Check, CheckId> {
    protected CheckService(JpaRepository<Check, CheckId> repository) {
        super(repository);
    }
}
