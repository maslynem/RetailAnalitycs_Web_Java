package ru.school.retailanalitycs_web_java.services.entityServices;

import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.school.retailanalitycs_web_java.entities.tables.Customer;
import ru.school.retailanalitycs_web_java.exceptions.notFoundExceptions.CustomerNotFoundException;
import ru.school.retailanalitycs_web_java.repositories.entityRepositories.CustomerRepository;

@Service
public class CustomerService extends BaseService<Customer, Long> {

    @Autowired
    protected CustomerService(CustomerRepository repository, EntityManager entityManager) {
        super(repository, entityManager);
    }

    @Override
    @Transactional
    public Customer update(Long id, Customer entity) {
        Customer customer = findById(id).orElseThrow(() -> new CustomerNotFoundException(id));
        entity.setId(customer.getId());
        return merge(entity);
    }
}
