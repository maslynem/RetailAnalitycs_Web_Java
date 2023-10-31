package ru.school.retailanalitycs_web_java.repositories.entityRepositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.school.retailanalitycs_web_java.entities.tables.Sku;
import ru.school.retailanalitycs_web_java.entities.tables.SkuGroup;
import ru.school.retailanalitycs_web_java.entities.tables.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    long countByChecks_Id_Sku(Sku sku);

    long countByChecks_Id_Sku_SkuGroup(SkuGroup skuGroup);

}