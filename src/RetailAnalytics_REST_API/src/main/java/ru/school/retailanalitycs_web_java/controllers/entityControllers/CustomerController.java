package ru.school.retailanalitycs_web_java.controllers.entityControllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.school.retailanalitycs_web_java.dto.entityDto.customerDto.CustomerDto;
import ru.school.retailanalitycs_web_java.entities.tables.Customer;
import ru.school.retailanalitycs_web_java.exceptions.notFoundExceptions.CustomerNotFoundException;
import ru.school.retailanalitycs_web_java.mapper.CustomerMapper;
import ru.school.retailanalitycs_web_java.services.entityServices.CustomerService;

import java.util.List;

@RestController
@RequestMapping("api/v1/customers")
public class CustomerController {
    private final CustomerService customerService;
    private final CustomerMapper customerMapper;

    @Autowired
    public CustomerController(CustomerService customerService, CustomerMapper customerMapper) {
        this.customerService = customerService;
        this.customerMapper = customerMapper;
    }

    @GetMapping
    public List<CustomerDto> findAllCustomers() {
        return customerService.findAll().stream().map(customerMapper::toDto).toList();
    }

    @GetMapping(params = {"page", "size"})
    public Page<CustomerDto> findAllCustomersByPage(@RequestParam("page") int page,
                                                    @RequestParam("size") int size) {
        return customerService.findAllByPage(page, size).map(customerMapper::toDto);
    }

    @GetMapping("/{id}")
    public CustomerDto findCustomerById(@PathVariable Long id) {
        return customerService.findById(id).map(customerMapper::toDto).orElseThrow(() -> new CustomerNotFoundException(id));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerDto save(@Valid @RequestBody CustomerDto customerDto) {
        Customer customer = customerMapper.toEntity(customerDto);
        Customer save = customerService.save(customer);
        customerDto.setId(save.getId());
        return customerDto;
    }

    @PostMapping(value = "all", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void saveAll(@Valid @RequestBody List<CustomerDto> customersDto) {
        List<Customer> customers = customersDto.stream().map(customerMapper::toEntity).toList();
        customerService.save(customers);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public CustomerDto update(@PathVariable Long id, @Valid @RequestBody CustomerDto customerDto) {
        Customer customer = customerMapper.toEntity(customerDto);
        Customer updated = customerService.update(id, customer);
        return customerMapper.toDto(updated);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        customerService.deleteById(id);
    }

}
