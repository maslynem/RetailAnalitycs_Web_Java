package ru.school.retailanalitycs_web_java.services;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.school.retailanalitycs_web_java.entities.tables.Sku;

public class SkuService extends BaseService<Sku, Integer> {
    protected SkuService(JpaRepository<Sku, Integer> repository) {
        super(repository);
    }
}
