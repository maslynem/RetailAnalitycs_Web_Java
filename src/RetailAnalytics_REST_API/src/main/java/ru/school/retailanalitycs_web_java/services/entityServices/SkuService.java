package ru.school.retailanalitycs_web_java.services.entityServices;

import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.school.retailanalitycs_web_java.entities.tables.Sku;
import ru.school.retailanalitycs_web_java.exceptions.notFoundExceptions.SkuNotFoundException;
import ru.school.retailanalitycs_web_java.repositories.entityRepositories.SkuRepository;

@Service
public class SkuService extends BaseService<Sku, Long> {

    @Autowired
    protected SkuService(SkuRepository repository, EntityManager entityManager) {
        super(repository, entityManager);
    }

    @Override
    @Transactional
    public Sku update(Long id, Sku entity) {
        Sku sku = findById(id).orElseThrow(() -> new SkuNotFoundException(id));
        entity.setId(sku.getId());
        return merge(entity);
    }
}
