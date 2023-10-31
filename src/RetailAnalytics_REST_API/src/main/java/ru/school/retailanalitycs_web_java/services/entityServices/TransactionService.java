package ru.school.retailanalitycs_web_java.services.entityServices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.school.retailanalitycs_web_java.entities.tables.Sku;
import ru.school.retailanalitycs_web_java.entities.tables.SkuGroup;
import ru.school.retailanalitycs_web_java.entities.tables.Transaction;
import ru.school.retailanalitycs_web_java.exceptions.notFoundExceptions.StoreNotFoundException;
import ru.school.retailanalitycs_web_java.repositories.entityRepositories.TransactionRepository;

@Service
public class TransactionService extends BaseService<Transaction, Long> {
    private final StoreService storeService;
    private final TransactionRepository repository;

    @Autowired
    protected TransactionService(TransactionRepository repository, StoreService service) {
        super(repository);
        this.storeService = service;
        this.repository = repository;
    }

    @Override
    @Transactional
    public Transaction save(Transaction entity) {
        Long transactionStoreId = entity.getTransactionStoreId();
        if (!storeService.existsByTransactionalStoreId(transactionStoreId)) {
            throw new StoreNotFoundException(transactionStoreId);
        }
        return super.save(entity);
    }

    @Transactional(readOnly = true)
    public long countBySku(Sku sku) {
        return repository.countByChecks_Id_Sku(sku);
    }


    @Transactional(readOnly = true)
    public long countBySkuGroup(SkuGroup skuGroup) {
        return repository.countByChecks_Id_Sku_SkuGroup(skuGroup);
    }
}
