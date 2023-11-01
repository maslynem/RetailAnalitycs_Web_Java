package ru.s21school.retailanalytics_web.controllers.viewControllers;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.s21school.retailanalytics_web.dto.PageDto;
import ru.s21school.retailanalytics_web.dto.viewDto.CustomerViewDto;
import ru.s21school.retailanalytics_web.services.viewServices.CustomerViewService;

import java.io.IOException;

@Controller
@RequestMapping("views/customers")
@Slf4j
public class CustomerViewController {
    private final CustomerViewService customerViewService;

    @Autowired
    public CustomerViewController(CustomerViewService customerViewService) {
        this.customerViewService = customerViewService;
    }

    @GetMapping
    public String getCustomersPage(@RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "30") int size,
                                   Model model) {
        PageDto<CustomerViewDto> customersPage = customerViewService.performGetPageRequest(page, size);
        model.addAttribute("entities", customersPage.getContent());
        model.addAttribute("totalPages", customersPage.getTotalPages());
        model.addAttribute("totalElements", customersPage.getTotalElements());
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        return "views/customers";
    }

    @GetMapping("export")
    public void exportToCsv(HttpServletResponse servletResponse) throws IOException {
        customerViewService.performExportToCsv(servletResponse);
    }
}
