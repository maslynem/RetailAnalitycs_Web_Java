package ru.school.retailanalitycs_web_java.services.entityServices;

import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.school.retailanalitycs_web_java.entities.tables.Store;
import ru.school.retailanalitycs_web_java.entities.tables.StoreId;
import ru.school.retailanalitycs_web_java.exceptions.duplicateValue.StoreDuplicateValueException;
import ru.school.retailanalitycs_web_java.exceptions.notFoundExceptions.StoreNotFoundException;
import ru.school.retailanalitycs_web_java.repositories.entityRepositories.StoreRepository;

import java.util.List;

@Service
@Transactional
public class StoreService extends BaseService<Store, StoreId> {
    private final StoreRepository storeRepository;

    @Autowired
    protected StoreService(StoreRepository storeRepository, EntityManager entityManager) {
        super(storeRepository, entityManager);
        this.storeRepository = storeRepository;
    }

    @Transactional(readOnly = true)
    public boolean existsByTransactionalStoreId(Long transactionalStoreId) {
        return storeRepository.existsByTransactionStoreId(transactionalStoreId);
    }

    @Override
    @Transactional
    public Store save(Store entity) {
        StoreId storeId = entity.getId();
        findById(storeId)
                .ifPresent(store -> {
                    throw new StoreDuplicateValueException(storeId);
                });
        return super.save(entity);
    }

    @Override
    @Transactional
    public Store update(StoreId id, Store entity) {
        Store store = findById(id).orElseThrow(() -> new StoreNotFoundException(id));
        entity.setId(store.getId());
        return merge(entity);
    }

    @Transactional(readOnly = true)
    public List<Store> getStoresWithTransactionalStoreId(Long customerPrimaryStore) {
        return storeRepository.findById_TransactionStoreId(customerPrimaryStore);
    }
}
