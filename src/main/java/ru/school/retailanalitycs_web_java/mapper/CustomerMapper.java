package ru.school.retailanalitycs_web_java.mapper;

import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import ru.school.retailanalitycs_web_java.dto.CustomerDto;
import ru.school.retailanalitycs_web_java.entities.tables.Customer;
import ru.school.retailanalitycs_web_java.exceptions.CustomerNotFoundException;
import ru.school.retailanalitycs_web_java.services.CustomerService;

@Mapper(componentModel = "spring")
public abstract class CustomerMapper extends GenericMapper<Customer, CustomerDto> {

    @Autowired
    private CustomerService customerService;

    //todo check
    public Integer map(Customer person) {
        return person.getId();
    }

    public Customer map(Integer id) {
        return customerService.findById(id).orElseThrow(() -> new CustomerNotFoundException(id));
    }
}
