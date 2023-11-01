package ru.school.retailanalitycs_web_java.services.entityServices;

import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.school.retailanalitycs_web_java.entities.tables.SkuGroup;
import ru.school.retailanalitycs_web_java.exceptions.duplicateValue.SkuGroupDuplicateValueException;
import ru.school.retailanalitycs_web_java.exceptions.notFoundExceptions.SkuGroupNotFoundException;
import ru.school.retailanalitycs_web_java.repositories.entityRepositories.SkuGroupRepository;

import java.util.Optional;

@Service
public class SkuGroupService extends BaseService<SkuGroup, Long> {
    private final SkuGroupRepository skuGroupRepository;

    @Autowired
    protected SkuGroupService(SkuGroupRepository skuGroupRepository,
                              EntityManager entityManager) {
        super(skuGroupRepository, entityManager);
        this.skuGroupRepository = skuGroupRepository;
    }

    @Override
    @Transactional
    public SkuGroup save(SkuGroup entity) {
        findByGroupName(entity.getGroupName())
                .ifPresent(skuGroup -> {
                    throw new SkuGroupDuplicateValueException(skuGroup.getGroupName(), skuGroup.getId());
                });
        return super.save(entity);
    }

    @Override
    @Transactional
    public SkuGroup update(Long id, SkuGroup entity) {
        SkuGroup skuGroup = findById(id).orElseThrow(() -> new SkuGroupNotFoundException(id));
        findByGroupName(entity.getGroupName())
                .ifPresent(obj -> {
                    throw new SkuGroupDuplicateValueException(obj.getGroupName(), obj.getId());
                });
        entity.setId(skuGroup.getId());
        return merge(entity);
    }

    @Transactional
    public Optional<SkuGroup> findByGroupName(String groupName) {
        return skuGroupRepository.findByGroupName(groupName);
    }
}
