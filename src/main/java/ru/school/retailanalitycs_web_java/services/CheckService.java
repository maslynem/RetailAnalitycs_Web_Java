package ru.school.retailanalitycs_web_java.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import ru.school.retailanalitycs_web_java.entities.tables.Check;
import ru.school.retailanalitycs_web_java.entities.tables.CheckId;

@Service
public class CheckService extends BaseService<Check, CheckId> {
    @Autowired
    protected CheckService(JpaRepository<Check, CheckId> repository) {
        super(repository);
    }
}
