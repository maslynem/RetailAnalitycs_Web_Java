package ru.school.retailanalitycs_web_java.services.viewServices;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.school.retailanalitycs_web_java.entities.views.groupView.GroupView;
import ru.school.retailanalitycs_web_java.entities.views.groupView.GroupViewId;
import ru.school.retailanalitycs_web_java.repositories.viewRepositories.GroupViewRepository;

import java.util.List;

@Service
public class GroupViewService extends ReadOnlyBaseService<GroupView, GroupViewId> {
    private final GroupViewRepository repository;
    protected GroupViewService(GroupViewRepository repository) {
        super(repository);
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<GroupView> findAllSortByCustomerId() {
        return repository.findAllByOrderByIdAsc();
    }
}
