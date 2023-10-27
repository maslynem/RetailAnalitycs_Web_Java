package ru.school.retailanalitycs_web_java.services.viewServices;

import org.springframework.stereotype.Service;
import ru.school.retailanalitycs_web_java.entities.views.CustomerView;
import ru.school.retailanalitycs_web_java.repositories.viewRepositories.CustomerViewRepository;

@Service
public class CustomerViewService extends ReadOnlyBaseService<CustomerView, Integer> {
    protected CustomerViewService(CustomerViewRepository repository) {
        super(repository);
    }
}
