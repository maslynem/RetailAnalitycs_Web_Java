package ru.school.retailanalitycs_web_java.services.entityServices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.school.retailanalitycs_web_java.entities.tables.Store;
import ru.school.retailanalitycs_web_java.entities.tables.StoreId;
import ru.school.retailanalitycs_web_java.exceptions.duplicateValue.StoreDuplicateValueException;
import ru.school.retailanalitycs_web_java.repositories.entityRepositories.StoreRepository;

@Service
public class StoreService extends BaseService<Store, StoreId> {

    @Autowired
    protected StoreService(StoreRepository repository) {
        super(repository);
    }

    public boolean existsByTransactionalStoreId(Integer transactionalStoreId) {
        return ((StoreRepository) repository).existsByTransactionStoreId(transactionalStoreId);
    }

    @Override
    public Store save(Store entity) {
        StoreId storeId = entity.getId();
        repository.findById(storeId)
                .ifPresent(store -> {
                    throw new StoreDuplicateValueException(storeId);
                });
        return super.save(entity);
    }
}
