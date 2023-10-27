package ru.school.retailanalitycs_web_java.services.entityServices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.school.retailanalitycs_web_java.entities.tables.SkuGroup;
import ru.school.retailanalitycs_web_java.exceptions.duplicateValue.SkuGroupDuplicateValueException;
import ru.school.retailanalitycs_web_java.repositories.entityRepositories.SkuGroupRepository;

@Service
public class SkuGroupService extends BaseService<SkuGroup, Integer> {

    private final SkuGroupRepository skuGroupRepository;

    @Autowired
    protected SkuGroupService(SkuGroupRepository repository,
                              SkuGroupRepository skuGroupRepository) {
        super(repository);
        this.skuGroupRepository = skuGroupRepository;
    }

    @Override
    public SkuGroup save(SkuGroup entity) {
        skuGroupRepository.findByGroupName(entity.getGroupName())
                .ifPresent(skuGroup -> {
                    throw new SkuGroupDuplicateValueException(skuGroup.getGroupName(), skuGroup.getId());
                });
        return super.save(entity);
    }
}
