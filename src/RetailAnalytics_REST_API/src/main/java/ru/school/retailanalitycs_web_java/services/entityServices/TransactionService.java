package ru.school.retailanalitycs_web_java.services.entityServices;

import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.school.retailanalitycs_web_java.entities.tables.Sku;
import ru.school.retailanalitycs_web_java.entities.tables.SkuGroup;
import ru.school.retailanalitycs_web_java.entities.tables.Transaction;
import ru.school.retailanalitycs_web_java.exceptions.notFoundExceptions.StoreNotFoundException;
import ru.school.retailanalitycs_web_java.exceptions.notFoundExceptions.TransactionNotFoundException;
import ru.school.retailanalitycs_web_java.repositories.entityRepositories.TransactionRepository;

@Service
public class TransactionService extends BaseService<Transaction, Long> {
    private final StoreService storeService;
    private final TransactionRepository transactionRepository;

    @Autowired
    protected TransactionService(TransactionRepository transactionRepository, StoreService service, EntityManager entityManager) {
        super(transactionRepository, entityManager);
        this.storeService = service;
        this.transactionRepository = transactionRepository;
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

    @Override
    @Transactional
    public Transaction update(Long id, Transaction entity) {
        Transaction transaction = findById(id).orElseThrow(() -> new TransactionNotFoundException(id));
        Long transactionStoreId = entity.getTransactionStoreId();
        if (!storeService.existsByTransactionalStoreId(transactionStoreId)) {
            throw new StoreNotFoundException(transactionStoreId);
        }
        entity.setId(transaction.getId());
        return merge(entity);
    }

    @Transactional(readOnly = true)
    public long countBySku(Sku sku) {
        return transactionRepository.countByChecks_Id_Sku(sku);
    }


    @Transactional(readOnly = true)
    public long countBySkuGroup(SkuGroup skuGroup) {
        return transactionRepository.countByChecks_Id_Sku_SkuGroup(skuGroup);
    }
}
