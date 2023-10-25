package ru.school.retailanalitycs_web_java.services;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import ru.school.retailanalitycs_web_java.entities.tables.Customer;

@Service
public class CustomerService extends BaseService<Customer, Integer> {

    protected CustomerService(JpaRepository<Customer, Integer> repository) {
        super(repository);
    }

}
