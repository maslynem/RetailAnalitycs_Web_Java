package ru.school.retailanalitycs_web_java.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.school.retailanalitycs_web_java.entities.tables.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
}