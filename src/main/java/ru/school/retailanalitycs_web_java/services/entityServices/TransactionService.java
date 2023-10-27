package ru.school.retailanalitycs_web_java.services.entityServices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.school.retailanalitycs_web_java.entities.tables.Transaction;
import ru.school.retailanalitycs_web_java.exceptions.notFoundExceptions.StoreNotFoundException;
import ru.school.retailanalitycs_web_java.repositories.entityRepositories.TransactionRepository;

@Service
public class TransactionService extends BaseService<Transaction, Long> {
    private final StoreService storeService;

    @Autowired
    protected TransactionService(TransactionRepository repository, StoreService service) {
        super(repository);
        this.storeService = service;
    }

    @Override
    public Transaction save(Transaction entity) {
        Long transactionStoreId = entity.getTransactionStoreId();
        if (!storeService.existsByTransactionalStoreId(transactionStoreId)) {
            throw new StoreNotFoundException(transactionStoreId);
        }
        return super.save(entity);
    }
}
