package ru.school.retailanalitycs_web_java.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.school.retailanalitycs_web_java.entities.views.CustomerView;
import ru.school.retailanalitycs_web_java.services.CustomerViewService;

import java.util.List;

@Controller
@RequestMapping("api/v1/customer-view")
public class CustomerViewController {

    private final CustomerViewService customerViewService;

    @Autowired
    public CustomerViewController(CustomerViewService customerViewService) {
        this.customerViewService = customerViewService;
    }

    @GetMapping
    public List<CustomerView> findAll() {
        return customerViewService.findAllCustomersFromView();
    }
}
