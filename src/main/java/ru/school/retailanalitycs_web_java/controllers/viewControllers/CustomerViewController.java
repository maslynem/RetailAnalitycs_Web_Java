package ru.school.retailanalitycs_web_java.controllers.viewControllers;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import ru.school.retailanalitycs_web_java.dto.viewDto.CustomerViewDto;
import ru.school.retailanalitycs_web_java.exceptions.notFoundExceptions.CustomerViewNotFoundException;
import ru.school.retailanalitycs_web_java.mapper.CustomerViewMapper;
import ru.school.retailanalitycs_web_java.services.viewServices.CustomerViewService;

import java.util.List;

@RestController
@RequestMapping("api/v1/views/customers")
public class CustomerViewController {
    private final CustomerViewService customerViewService;
    private final CustomerViewMapper customerViewMapper;

    public CustomerViewController(CustomerViewService customerViewService, CustomerViewMapper customerViewMapper) {
        this.customerViewService = customerViewService;
        this.customerViewMapper = customerViewMapper;
    }

    @GetMapping
    public List<CustomerViewDto> findAllCustomers() {
        return customerViewService.findAll().stream().map(customerViewMapper::toDto).toList();
    }

    @GetMapping(params = {"page", "size"})
    public Page<CustomerViewDto> findAllCustomersByPage(@RequestParam("page") int page,
                                                        @RequestParam("size") int size) {
        return customerViewService.findAllByPage(page, size).map(customerViewMapper::toDto);
    }

    @GetMapping("/{id}")
    public CustomerViewDto findCustomerById(@PathVariable Long id) {
        return customerViewService.findById(id).map(customerViewMapper::toDto).orElseThrow(() -> new CustomerViewNotFoundException(id));
    }

}
