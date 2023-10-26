package ru.school.retailanalitycs_web_java.mapper;

import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import ru.school.retailanalitycs_web_java.dto.customerDto.CustomerDto;
import ru.school.retailanalitycs_web_java.entities.tables.Customer;
import ru.school.retailanalitycs_web_java.exceptions.notFoundExceptions.CustomerNotFoundException;
import ru.school.retailanalitycs_web_java.services.CustomerService;

@Mapper(componentModel = "spring", uses = {CustomerMapper.class})
public abstract class CustomerMapper {
    @Autowired
    private CustomerService customerService;

    public abstract CustomerDto toDto(Customer customer);

    public abstract Customer toEntity(CustomerDto dto);

    public Customer toDtoById(Integer customerID) {
        return customerService.findById(customerID).orElseThrow(() -> new CustomerNotFoundException(customerID));
    }
}
