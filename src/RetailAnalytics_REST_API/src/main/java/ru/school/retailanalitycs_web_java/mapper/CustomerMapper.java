package ru.school.retailanalitycs_web_java.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import ru.school.retailanalitycs_web_java.dto.entityDto.customerDto.CustomerDto;
import ru.school.retailanalitycs_web_java.entities.tables.Customer;
import ru.school.retailanalitycs_web_java.exceptions.notFoundExceptions.CustomerNotFoundException;
import ru.school.retailanalitycs_web_java.services.entityServices.CustomerService;

@Mapper(componentModel = "spring")
public abstract class CustomerMapper {
    @Autowired
    private CustomerService customerService;

    public abstract CustomerDto toDto(Customer entity);

    @Mapping(target = "cards", ignore = true)
    public abstract Customer toEntity(CustomerDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "cards", ignore = true)
    public abstract Customer merge(@MappingTarget Customer customer, CustomerDto customerDto);

    public Customer toDtoById(Long customerID) {
        return customerService.findById(customerID).orElseThrow(() -> new CustomerNotFoundException(customerID));
    }
}
