package ru.school.retailanalitycs_web_java.repositories.entityRepositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.school.retailanalitycs_web_java.entities.tables.Sku;
import ru.school.retailanalitycs_web_java.entities.tables.SkuGroup;
import ru.school.retailanalitycs_web_java.entities.tables.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    long countByChecks_Sku_SkuGroup(SkuGroup skuGroup);

    long countByChecks_Sku(Sku sku);
}