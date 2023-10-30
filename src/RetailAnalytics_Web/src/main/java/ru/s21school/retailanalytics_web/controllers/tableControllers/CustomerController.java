package ru.s21school.retailanalytics_web.controllers.tableControllers;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import ru.s21school.retailanalytics_web.dto.ErrorDto;
import ru.s21school.retailanalytics_web.dto.entityDto.PageDto;
import ru.s21school.retailanalytics_web.dto.entityDto.customerDto.CustomerDto;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Optional;

@Controller
@RequestMapping("data/customers")
@Slf4j
public class CustomerController {
    private static final String CUSTOMER_API_URL = "http://localhost:8081/api/v1/customers";

    private final RestTemplate restTemplate;
    private final ImportExportHandler importExportHandler;

    public CustomerController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.importExportHandler = new ImportExportHandler(CUSTOMER_API_URL, "customers", restTemplate);

    }

    @GetMapping
    public String getCustomersPage(@RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "30") int size,
                                   Model model) {
        ResponseEntity<PageDto<CustomerDto>> response =
                restTemplate.exchange(CUSTOMER_API_URL + String.format("?page=%d&size=%d", page, size),
                        HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                        });
        PageDto<CustomerDto> customersPage = Optional.ofNullable(response.getBody()).orElseThrow();
        model.addAttribute("entities", customersPage.getContent());
        model.addAttribute("totalPages", customersPage.getTotalPages());
        model.addAttribute("totalElements", customersPage.getTotalElements());
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        return "tables/customers/customers";
    }

    @GetMapping("new")
    public String getCreateCustomerPage(Model model) {
        model.addAttribute("customer", new CustomerDto());
        return "tables/customers/new";
    }

    @PostMapping
    public String createCustomer(@Valid @ModelAttribute("customer") CustomerDto customer, Model model) {
        try {
            HttpEntity<CustomerDto> httpEntity = new HttpEntity<>(customer);
            ResponseEntity<LinkedHashMap<String, Object>> response =
                    restTemplate.exchange(CUSTOMER_API_URL,
                            HttpMethod.POST, httpEntity, new ParameterizedTypeReference<>() {
                            });
            LinkedHashMap<String, Object> map = response.getBody();
            log.info("New customer was saved. Id: {}", map.get("id"));
            return "redirect:/data/customers";
        } catch (HttpClientErrorException exception) {
            ErrorDto errorDto = exception.getResponseBodyAs(ErrorDto.class);
            log.warn(errorDto.getMessages().toString());
            model.addAttribute("errors", errorDto.getMessages());
            return "tables/customers/new";
        }
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable String id) {
        restTemplate.delete(CUSTOMER_API_URL + "/" + id);
        return "redirect:/data/customers";
    }

    @GetMapping("/export")
    public void exportToCsv(HttpServletResponse servletResponse) throws IOException {
        importExportHandler.exportToCsv(servletResponse);
    }

    @PostMapping("/import")
    public String importFromCsv(@RequestParam MultipartFile file) {
        importExportHandler.importFromCsv(file);
        return "redirect:/data/customers";
    }
}
