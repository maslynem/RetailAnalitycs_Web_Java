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
import ru.s21school.retailanalytics_web.dto.viewDto.PeriodViewDto;
import ru.s21school.retailanalytics_web.services.viewServices.PeriodViewService;

import java.io.IOException;

@Controller
@RequestMapping("views/periods")
@Slf4j
public class PeriodViewController {
    private final PeriodViewService periodViewService;

    @Autowired
    public PeriodViewController(PeriodViewService periodViewService) {
        this.periodViewService = periodViewService;
    }

    @GetMapping
    public String getPeriodViewPage(@RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "30") int size,
                                    Model model) {
        PageDto<PeriodViewDto> periodsPage = periodViewService.performGetPageRequest(page, size);
        model.addAttribute("entities", periodsPage.getContent());
        model.addAttribute("totalPages", periodsPage.getTotalPages());
        model.addAttribute("totalElements", periodsPage.getTotalElements());
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        return "views/periods";
    }

    @GetMapping("export")
    public void exportToCsv(HttpServletResponse servletResponse) throws IOException {
        periodViewService.performExportToCsv(servletResponse);
    }
}
