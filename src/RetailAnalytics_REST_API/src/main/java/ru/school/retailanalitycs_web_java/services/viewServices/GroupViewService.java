package ru.school.retailanalitycs_web_java.services.viewServices;

import org.springframework.stereotype.Service;
import ru.school.retailanalitycs_web_java.entities.views.groupView.GroupView;
import ru.school.retailanalitycs_web_java.entities.views.groupView.GroupViewId;
import ru.school.retailanalitycs_web_java.repositories.viewRepositories.GroupViewRepository;

@Service
public class GroupViewService extends ReadOnlyBaseService<GroupView, GroupViewId> {
    protected GroupViewService(GroupViewRepository repository) {
        super(repository);
    }
}
