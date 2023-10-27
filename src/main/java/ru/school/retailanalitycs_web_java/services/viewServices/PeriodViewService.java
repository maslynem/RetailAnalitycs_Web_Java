package ru.school.retailanalitycs_web_java.services.viewServices;

import org.springframework.stereotype.Service;
import ru.school.retailanalitycs_web_java.entities.views.PeriodView;
import ru.school.retailanalitycs_web_java.entities.views.PeriodViewId;
import ru.school.retailanalitycs_web_java.repositories.viewRepositories.PeriodViewRepository;

@Service
public class PeriodViewService extends ReadOnlyBaseService<PeriodView, PeriodViewId> {
    protected PeriodViewService(PeriodViewRepository repository) {
        super(repository);
    }
}
