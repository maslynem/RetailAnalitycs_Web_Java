package ru.school.retailanalitycs_web_java.services.entityServices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.school.retailanalitycs_web_java.entities.tables.Sku;
import ru.school.retailanalitycs_web_java.repositories.entityRepositories.SkuRepository;

@Service
public class SkuService extends BaseService<Sku, Long> {
    @Autowired
    protected SkuService(SkuRepository repository) {
        super(repository);
    }
}
