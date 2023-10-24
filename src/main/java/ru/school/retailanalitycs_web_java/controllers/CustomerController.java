package ru.school.retailanalitycs_web_java.controllers;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.school.retailanalitycs_web_java.dto.CustomerDto;
import ru.school.retailanalitycs_web_java.entities.tables.Customer;
import ru.school.retailanalitycs_web_java.exceptions.CustomerNotFoundException;
import ru.school.retailanalitycs_web_java.mapper.CustomerMapper;
import ru.school.retailanalitycs_web_java.services.CustomerService;
import ru.school.retailanalitycs_web_java.utils.CsvReader;

import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("api/v1/customers")
public class CustomerController {
    private final CustomerService customerService;
    private final CustomerMapper customerMapper;
    private final CsvReader<CustomerDto> csvReader;

    @Autowired
    public CustomerController(CustomerService customerService, CustomerMapper customerMapper, CsvReader<CustomerDto> csvReader) {
        this.customerService = customerService;
        this.customerMapper = customerMapper;
        this.csvReader = csvReader;
    }

    @GetMapping
    public List<CustomerDto> findAllCustomers() {
        return customerService.findAll().stream().map(customerMapper::toDto).toList();
    }

    @GetMapping("all")
    public Page<CustomerDto> findAllByPage(@RequestParam("page") int page,
                                           @RequestParam("size") int size) {
        return customerService.findAllByPage(page, size).map(customerMapper::toDto);
    }

    @GetMapping("/{id}")
    public CustomerDto findCustomerById(@PathVariable Integer id) {
        return customerService.findById(id).map(customerMapper::toDto).orElseThrow(() -> new CustomerNotFoundException(id));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerDto create(@RequestBody CustomerDto customerDto) {
        Customer customer = customerMapper.toEntity(customerDto);
        Customer save = customerService.save(customer);
        customerDto.setId(save.getId());
        return customerDto;
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Integer id) {
        customerService.deleteById(id);
    }

    @PostMapping(value = "import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @SneakyThrows
    public void importFromCsv(@RequestPart MultipartFile file) {
        InputStream inputStream = file.getInputStream();
        List<Customer> cards = csvReader.importCsv(inputStream, CustomerDto.class).stream().map(customerMapper::toEntity).toList();
        customerService.save(cards);
    }

}
