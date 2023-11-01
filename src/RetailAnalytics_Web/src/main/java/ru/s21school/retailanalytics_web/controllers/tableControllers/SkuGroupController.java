package ru.s21school.retailanalytics_web.controllers.tableControllers;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;
import ru.s21school.retailanalytics_web.dto.ErrorDto;
import ru.s21school.retailanalytics_web.dto.entityDto.skuGroupDto.SkuGroupDto;
import ru.s21school.retailanalytics_web.dto.entityDto.skuGroupDto.SkuGroupPageDto;
import ru.s21school.retailanalytics_web.services.tableServices.SkuGroupService;

import java.io.IOException;

@Controller
@RequestMapping("data/sku-groups")
@Slf4j
public class SkuGroupController {
    private final SkuGroupService skuGroupService;

    public SkuGroupController(SkuGroupService skuGroupService) {
        this.skuGroupService = skuGroupService;
    }


    @GetMapping
    public String getSkuGroupsPage(@RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "30") int size,
                                   Model model) {
        SkuGroupPageDto skuGroupPage = skuGroupService.performGetPageRequest(page, size);
        model.addAttribute("entities", skuGroupPage.getContent());
        model.addAttribute("totalPages", skuGroupPage.getTotalPages());
        model.addAttribute("totalElements", skuGroupPage.getTotalElements());
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        return "tables/skuGroups/skuGroups";
    }

    @GetMapping("new")
    public String getCreateSkuGroupPage(Model model) {
        model.addAttribute("skuGroup", new SkuGroupDto());
        return "tables/skuGroups/new";
    }

    @PostMapping
    public String createSkuGroup(@Valid @ModelAttribute("skuGroup") SkuGroupDto skuGroup, Model model) {
        try {
            skuGroupService.performSaveSkuGroupRequest(skuGroup);
            return "redirect:/data/sku-groups";
        } catch (HttpClientErrorException exception) {
            ErrorDto errorDto = exception.getResponseBodyAs(ErrorDto.class);
            log.warn(errorDto.getMessages().toString());
            model.addAttribute("errors", errorDto.getMessages());
            return "tables/skuGroups/new";
        }
    }

    @GetMapping("{id}")
    public String getUpdateSkuGroupPage(@PathVariable Long id, Model model) {
        SkuGroupDto skuGroup = skuGroupService.performFindByIdRequest(id);
        model.addAttribute("skuGroup", skuGroup);
        return "tables/skuGroups/update";
    }

    @PutMapping("{id}")
    public String updateSkuGroup(@PathVariable Long id,
                                 @ModelAttribute("skuGroup") SkuGroupDto skuGroup,
                                 Model model) {
        try {
            skuGroupService.performUpdateSkuGroupRequest(id, skuGroup);
            return "redirect:/data/sku-groups";
        } catch (HttpClientErrorException exception) {
            ErrorDto errorDto = exception.getResponseBodyAs(ErrorDto.class);
            log.warn(errorDto.getMessages().toString());
            model.addAttribute("errors", errorDto.getMessages());
            return "tables/skuGroups/update";
        }
    }
    
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        skuGroupService.performDeleteByIdRequest(id);
        return "redirect:/data/sku-groups";
    }

    @GetMapping("/export")
    public void exportToCsv(HttpServletResponse servletResponse) throws IOException {
        skuGroupService.performExportToCsv(servletResponse);

    }

    @PostMapping("/import")
    public String importFromCsv(@RequestParam MultipartFile file) {
        skuGroupService.performImportFromCsv(file);

        return "redirect:/data/sku-groups";
    }
}
