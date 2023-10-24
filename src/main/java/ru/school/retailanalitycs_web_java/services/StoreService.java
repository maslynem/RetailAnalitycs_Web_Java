package ru.school.retailanalitycs_web_java.services;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.school.retailanalitycs_web_java.entities.tables.CheckId;
import ru.school.retailanalitycs_web_java.entities.tables.Store;

public class StoreService extends BaseService<Store, CheckId> {
    protected StoreService(JpaRepository<Store, CheckId> repository) {
        super(repository);
    }
}
