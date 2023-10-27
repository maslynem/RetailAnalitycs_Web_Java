package ru.school.retailanalitycs_web_java.services.entityServices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.school.retailanalitycs_web_java.entities.tables.Customer;
import ru.school.retailanalitycs_web_java.repositories.entityRepositories.CustomerRepository;

@Service
public class CustomerService extends BaseService<Customer, Long> {
    @Autowired
    protected CustomerService(CustomerRepository repository) {
        super(repository);
    }

}
