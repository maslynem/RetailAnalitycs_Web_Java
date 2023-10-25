package ru.school.retailanalitycs_web_java.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import ru.school.retailanalitycs_web_java.entities.tables.SkuGroup;

@Service
public class SkuGroupService extends BaseService<SkuGroup, Integer> {
    @Autowired
    protected SkuGroupService(JpaRepository<SkuGroup, Integer> repository) {
        super(repository);
    }
}
