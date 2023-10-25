package ru.school.retailanalitycs_web_java.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.school.retailanalitycs_web_java.entities.tables.Store;
import ru.school.retailanalitycs_web_java.entities.tables.StoreId;
import ru.school.retailanalitycs_web_java.repositories.StoreRepository;

@Service
public class StoreService extends BaseService<Store, StoreId> {

    @Autowired
    protected StoreService(StoreRepository repository) {
        super(repository);
    }

    public boolean existsByTransactionalStoreId(Integer transactionalStoreId) {
        return ((StoreRepository) repository).existsByTransactionStoreId(transactionalStoreId);
    }
}
