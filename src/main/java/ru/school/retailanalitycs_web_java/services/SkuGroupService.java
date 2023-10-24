package ru.school.retailanalitycs_web_java.services;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.school.retailanalitycs_web_java.entities.tables.SkuGroup;

public class SkuGroupService extends BaseService<SkuGroup, Integer> {
    protected SkuGroupService(JpaRepository<SkuGroup, Integer> repository) {
        super(repository);
    }
}
