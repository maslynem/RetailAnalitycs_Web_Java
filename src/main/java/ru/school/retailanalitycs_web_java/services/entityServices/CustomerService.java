package ru.school.retailanalitycs_web_java.services.entityServices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import ru.school.retailanalitycs_web_java.entities.tables.Customer;

@Service
public class CustomerService extends BaseService<Customer, Integer> {
    @Autowired
    protected CustomerService(JpaRepository<Customer, Integer> repository) {
        super(repository);
    }

}
