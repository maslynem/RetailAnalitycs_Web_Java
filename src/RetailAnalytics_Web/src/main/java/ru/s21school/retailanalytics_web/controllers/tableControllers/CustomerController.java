package ru.s21school.retailanalytics_web.controllers.tableControllers;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;
import ru.s21school.retailanalytics_web.dto.ErrorDto;
import ru.s21school.retailanalytics_web.dto.entityDto.customerDto.CustomerDto;
import ru.s21school.retailanalytics_web.dto.entityDto.customerDto.CustomerPageDto;
import ru.s21school.retailanalytics_web.services.tableServices.CustomerService;

import java.io.IOException;
import java.util.Collections;

@Controller
@RequestMapping("data/customers")
@Slf4j
public class CustomerController {
    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }


    @GetMapping
    public String getCustomersPage(@RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "30") int size,
                                   Model model) {
        CustomerPageDto customerPage = customerService.performGetPageRequest(page, size);
        model.addAttribute("entities", customerPage.getContent());
        model.addAttribute("totalPages", customerPage.getTotalPages());
        model.addAttribute("totalElements", customerPage.getTotalElements());
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
            customerService.performSaveCustomerRequest(customer);
            return "redirect:/data/customers";
        } catch (HttpClientErrorException exception) {
            ErrorDto errorDto = exception.getResponseBodyAs(ErrorDto.class);
            log.warn(errorDto.getMessages().toString());
            model.addAttribute("errors", errorDto.getMessages());
            return "tables/customers/new";
        }
    }

    @GetMapping("{id}")
    public String getUpdateCustomerPage(@PathVariable Long id, Model model) {
        CustomerDto customer = customerService.performFindByIdRequest(id);
        model.addAttribute("customer", customer);
        return "tables/customers/update";
    }

    @PutMapping("{id}")
    public String updateCustomer(@PathVariable Long id,
                                 @ModelAttribute("customer") CustomerDto customer,
                                 Model model) {
        try {
            customerService.performUpdateCustomerRequest(id, customer);
            return "redirect:/data/customers";
        } catch (HttpClientErrorException exception) {
            ErrorDto errorDto = exception.getResponseBodyAs(ErrorDto.class);
            log.warn(errorDto.getMessages().toString());
            model.addAttribute("errors", errorDto.getMessages());
            return "tables/customers/update";
        }
    }
    
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        customerService.performDeleteByIdRequest(id);
        return "redirect:/data/customers";
    }

    @GetMapping("/export")
    public void exportToCsv(HttpServletResponse servletResponse) throws IOException {
        customerService.performExportToCsv(servletResponse);
    }

    @PostMapping("/import")
    public String importFromCsv(@RequestParam MultipartFile file, Model model) {
        try {
            customerService.performImportFromCsv(file.getInputStream());
            return "redirect:/data/customers";
        } catch (IOException e) {
            log.warn("IOException when importFromCsv called");
            model.addAttribute("errors", Collections.singletonList("Error during import. Try again"));
        } catch (HttpClientErrorException exception) {
            ErrorDto errorDto = exception.getResponseBodyAs(ErrorDto.class);
            log.warn(errorDto.getMessages().toString());
            model.addAttribute("errors", errorDto.getMessages());
        }
        return "tables/customers/customers";
    }
}
