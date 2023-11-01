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
import ru.s21school.retailanalytics_web.dto.viewDto.GroupViewDto;
import ru.s21school.retailanalytics_web.services.viewServices.GroupViewService;

import java.io.IOException;

@Controller
@RequestMapping("views/groups")
@Slf4j
public class GroupViewController {
    private final GroupViewService groupViewService;

    @Autowired
    public GroupViewController(GroupViewService groupViewService) {
        this.groupViewService = groupViewService;
    }


    @GetMapping
    public String getGroupViewPage(@RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "30") int size,
                                   Model model) {
        PageDto<GroupViewDto> groupsPage = groupViewService.performGetPageRequest(page, size);
        model.addAttribute("entities", groupsPage.getContent());
        model.addAttribute("totalPages", groupsPage.getTotalPages());
        model.addAttribute("totalElements", groupsPage.getTotalElements());
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        return "views/groups";
    }

    @GetMapping("export")
    public void exportToCsv(HttpServletResponse servletResponse) throws IOException {
        groupViewService.performExportToCsv(servletResponse);
    }
}
