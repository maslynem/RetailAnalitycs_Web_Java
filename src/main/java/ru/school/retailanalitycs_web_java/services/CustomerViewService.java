package ru.school.retailanalitycs_web_java.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.school.retailanalitycs_web_java.entities.views.CustomerView;
import ru.school.retailanalitycs_web_java.repositories.CustomerViewRepository;

import java.util.List;

@Service
public class CustomerViewService {
    private final CustomerViewRepository customerViewRepository;

    @Autowired
    public CustomerViewService(CustomerViewRepository customerViewRepository) {
        this.customerViewRepository = customerViewRepository;
    }

    public List<CustomerView> findAllCustomersFromView() {
        return customerViewRepository.findAll();
    }
}
