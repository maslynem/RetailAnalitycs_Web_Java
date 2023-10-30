package ru.school.retailanalitycs_web_java.services.entityServices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.school.retailanalitycs_web_java.entities.tables.Store;
import ru.school.retailanalitycs_web_java.entities.tables.StoreId;
import ru.school.retailanalitycs_web_java.exceptions.duplicateValue.StoreDuplicateValueException;
import ru.school.retailanalitycs_web_java.repositories.entityRepositories.StoreRepository;

import java.util.List;

@Service
public class StoreService extends BaseService<Store, StoreId> {
    private final StoreRepository repository;

    @Autowired
    protected StoreService(StoreRepository repository) {
        super(repository);
        this.repository = repository;
    }

    public boolean existsByTransactionalStoreId(Long transactionalStoreId) {
        return repository.existsByTransactionStoreId(transactionalStoreId);
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

    public List<Store> getStoresWithTransactionalStoreId(Long customerPrimaryStore) {
        return repository.findById_TransactionStoreId(customerPrimaryStore);
    }
}
