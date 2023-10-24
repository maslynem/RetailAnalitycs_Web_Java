package ru.school.retailanalitycs_web_java.services;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.school.retailanalitycs_web_java.entities.tables.Transaction;

public class TransactionService extends BaseService<Transaction, Integer> {
    protected TransactionService(JpaRepository<Transaction, Integer> repository) {
        super(repository);
    }
}
