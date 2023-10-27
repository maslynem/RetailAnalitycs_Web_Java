package ru.school.retailanalitycs_web_java.repositories.entityRepositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.school.retailanalitycs_web_java.entities.tables.Sku;

public interface SkuRepository extends JpaRepository<Sku, Long> {
}