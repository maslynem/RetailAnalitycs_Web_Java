package ru.school.retailanalitycs_web_java.repositories.entityRepositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.school.retailanalitycs_web_java.entities.tables.Store;
import ru.school.retailanalitycs_web_java.entities.tables.StoreId;

import java.util.List;

public interface StoreRepository extends JpaRepository<Store, StoreId> {
    @Query("select s from Store s join fetch s.id.sku join fetch s.id.sku.skuGroup where s.id.transactionStoreId = ?1")
    List<Store> findById_TransactionStoreId(Long transactionStoreId);

    @Query("select (count(s) > 0) from Store s where s.id.transactionStoreId = ?1")
    boolean existsByTransactionStoreId(Long transactionStoreId);

}