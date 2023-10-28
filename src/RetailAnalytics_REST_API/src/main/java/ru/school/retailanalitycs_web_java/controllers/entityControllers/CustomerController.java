package ru.school.retailanalitycs_web_java.controllers.entityControllers;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.school.retailanalitycs_web_java.dto.entityDto.customerDto.CustomerDto;
import ru.school.retailanalitycs_web_java.entities.tables.Customer;
import ru.school.retailanalitycs_web_java.exceptions.notFoundExceptions.CustomerNotFoundException;
import ru.school.retailanalitycs_web_java.mapper.CustomerMapper;
import ru.school.retailanalitycs_web_java.services.entityServices.CustomerService;
import ru.school.retailanalitycs_web_java.utils.CsvReader;
import ru.school.retailanalitycs_web_java.utils.CsvWriter;

import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("api/v1/customers")
public class CustomerController {
    private final CustomerService customerService;
    private final CustomerMapper customerMapper;
    private final CsvReader<CustomerDto> csvReader;
    private final CsvWriter<CustomerDto> csvWriter;

    @Autowired
    public CustomerController(CustomerService customerService, CustomerMapper customerMapper, CsvReader<CustomerDto> csvReader, CsvWriter<CustomerDto> csvWriter) {
        this.customerService = customerService;
        this.customerMapper = customerMapper;
        this.csvReader = csvReader;
        this.csvWriter = csvWriter;
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
    public CustomerDto create(@Valid @RequestBody CustomerDto customerDto) {
        Customer customer = customerMapper.toEntity(customerDto);
        Customer save = customerService.save(customer);
        customerDto.setId(save.getId());
        return customerDto;
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        customerService.deleteById(id);
    }

    @PostMapping(value = "import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @SneakyThrows
    public void importFromCsv(@RequestPart MultipartFile file) {
        InputStream inputStream = file.getInputStream();
        List<Customer> customers = csvReader.importCsv(inputStream, CustomerDto.class).stream().map(customerMapper::toEntity).toList();
        customerService.save(customers);
    }

    @GetMapping(value = "export")
    @SneakyThrows
    public void exportToCsv(HttpServletResponse servletResponse) {
        servletResponse.setContentType("text/csv");
        servletResponse.addHeader("Content-Disposition", "attachment; filename=\"customer.tsv\"");
        List<CustomerDto> customers = customerService.findAll().stream().map(customerMapper::toDto).toList();
        csvWriter.exportCsv(servletResponse.getWriter(), customers, CustomerDto.class);
    }
}
