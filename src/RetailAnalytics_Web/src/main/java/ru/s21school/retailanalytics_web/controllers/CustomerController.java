package ru.s21school.retailanalytics_web.controllers;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import ru.s21school.retailanalytics_web.dto.entityDto.customerDto.CustomerDto;
import ru.s21school.retailanalytics_web.dto.entityDto.customerDto.CustomerPageDto;

import java.io.IOException;
import java.util.Optional;

@Controller
@RequestMapping("data/customers")
@RequiredArgsConstructor
@Slf4j
public class CustomerController {
    private static final String CUSTOMER_API_URL = "http://localhost:8081/api/v1/customers";

    private final RestTemplate restTemplate;

    @GetMapping
    public String getCustomersPage(@RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "30") int size,
                                   Model model) {
        ResponseEntity<CustomerPageDto> response =
                restTemplate.exchange(CUSTOMER_API_URL + String.format("?page=%d&size=%d", page, size),
                        HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                        });
        CustomerPageDto customersPage = Optional.ofNullable(response.getBody()).orElse(new CustomerPageDto());
        model.addAttribute("entities", customersPage.getContent());
        model.addAttribute("totalPages", customersPage.getTotalPages());
        model.addAttribute("totalElements", customersPage.getTotalElements());
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        return "customer/customers";
    }

    @GetMapping("new")
    public String getCreateCustomerPage(Model model) {
        model.addAttribute("customer", new CustomerDto());
        return "customer/new";
    }

    @PostMapping
    public String createCustomer(@Valid @ModelAttribute("customer") CustomerDto customer, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.warn("New customer is not valid: {}", bindingResult.getAllErrors());
            return "customer/new";
        }
        ResponseEntity<CustomerDto> customerDtoResponseEntity = restTemplate.postForEntity(CUSTOMER_API_URL, customer, CustomerDto.class);
        log.info("New customer was saved. Id: {}", customerDtoResponseEntity.getBody().getId());
        return "redirect:/data/customers";
    }


    @DeleteMapping("/{id}")
    public String delete(@PathVariable String id) {
        restTemplate.delete(CUSTOMER_API_URL + "/" + id);
        return "redirect:/data/customers";
    }

    @GetMapping("/export")
    public void exportToCsv(HttpServletResponse servletResponse) throws IOException {
        servletResponse.sendRedirect(CUSTOMER_API_URL + "/export");
    }

    @PostMapping("/import")
    public String importFromCsv(@RequestParam MultipartFile file) {
        Resource invoicesResource = file.getResource();
        LinkedMultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();
        parts.add("file", invoicesResource);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<LinkedMultiValueMap<String, Object>> httpEntity = new HttpEntity<>(parts, httpHeaders);
        String url = CUSTOMER_API_URL + "/import";
        restTemplate.postForEntity(url, httpEntity, Object.class);
        return "redirect:/peers/";
    }
}
