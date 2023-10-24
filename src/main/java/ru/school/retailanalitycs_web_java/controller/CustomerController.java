package ru.school.retailanalitycs_web_java.controller;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.school.retailanalitycs_web_java.dto.CardDto;
import ru.school.retailanalitycs_web_java.dto.CustomerDto;
import ru.school.retailanalitycs_web_java.entities.tables.Card;
import ru.school.retailanalitycs_web_java.entities.tables.Customer;
import ru.school.retailanalitycs_web_java.mapper.CustomerMapper;
import ru.school.retailanalitycs_web_java.services.CustomerService;
import ru.school.retailanalitycs_web_java.utils.CsvReader;

import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("api/v1/customer")
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

    @PostMapping(value = "import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @SneakyThrows
    public void importFromCsv(@RequestPart MultipartFile file) {
        InputStream inputStream = file.getInputStream();
        List<Customer> cards = csvReader.importCsv(inputStream, CustomerDto.class).stream().map(customerMapper::toEntity).toList();
        customerService.save(cards);
    }

}
