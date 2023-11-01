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
import ru.s21school.retailanalytics_web.dto.viewDto.PurchaseHistoryViewDto;
import ru.s21school.retailanalytics_web.services.viewServices.PurchaseHistoryViewService;

import java.io.IOException;

@Controller
@RequestMapping("views/purchase-histories")
@Slf4j
public class PurchaseHistoryViewController {
    private final PurchaseHistoryViewService purchaseHistoryViewService;

    @Autowired
    public PurchaseHistoryViewController(PurchaseHistoryViewService purchaseHistoryViewService) {
        this.purchaseHistoryViewService = purchaseHistoryViewService;
    }

    @GetMapping
    public String getPurchaseHistoryViewPage(@RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "30") int size,
                                             Model model) {
        PageDto<PurchaseHistoryViewDto> purchaseHistoriesPage = purchaseHistoryViewService.performGetPageRequest(page, size);
        model.addAttribute("entities", purchaseHistoriesPage.getContent());
        model.addAttribute("totalPages", purchaseHistoriesPage.getTotalPages());
        model.addAttribute("totalElements", purchaseHistoriesPage.getTotalElements());
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        return "views/purchaseHistory";
    }

    @GetMapping("export")
    public void exportToCsv(HttpServletResponse servletResponse) throws IOException {
        purchaseHistoryViewService.performExportToCsv(servletResponse);
    }
}
