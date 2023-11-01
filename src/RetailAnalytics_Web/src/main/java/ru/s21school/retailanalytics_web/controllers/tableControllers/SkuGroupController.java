package ru.s21school.retailanalytics_web.controllers.tableControllers;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;
import ru.s21school.retailanalytics_web.dto.ErrorDto;
import ru.s21school.retailanalytics_web.dto.PageDto;
import ru.s21school.retailanalytics_web.dto.entityDto.skuGroupDto.SkuGroupDto;
import ru.s21school.retailanalytics_web.services.tableServices.SkuGroupService;

import java.io.IOException;
import java.util.Collections;

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
        PageDto<SkuGroupDto> skuGroupPage = skuGroupService.performGetPageRequest(page, size);
        model.addAttribute("entities", skuGroupPage.getContent());
        model.addAttribute("totalPages", skuGroupPage.getTotalPages());
        model.addAttribute("totalElements", skuGroupPage.getTotalElements());
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        return "tables/skuGroups/skuGroups";
    }

    @GetMapping("new")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String getCreateSkuGroupPage(Model model) {
        model.addAttribute("skuGroup", new SkuGroupDto());
        return "tables/skuGroups/new";
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
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
    @PreAuthorize("hasAuthority('ADMIN')")
    public String getUpdateSkuGroupPage(@PathVariable Long id, Model model) {
        SkuGroupDto skuGroup = skuGroupService.performFindByIdRequest(id);
        model.addAttribute("skuGroup", skuGroup);
        return "tables/skuGroups/update";
    }

    @PutMapping("{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
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
    @PreAuthorize("hasAuthority('ADMIN')")
    public String delete(@PathVariable Long id) {
        skuGroupService.performDeleteByIdRequest(id);
        return "redirect:/data/sku-groups";
    }

    @GetMapping("/export")
    public void exportToCsv(HttpServletResponse servletResponse) throws IOException {
        skuGroupService.performExportToCsv(servletResponse);
    }

    @PostMapping("/import")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String importFromCsv(@RequestParam MultipartFile file, Model model) {
        try {
            skuGroupService.performImportFromCsv(file.getInputStream());
            return "redirect:/data/skuGroups";
        } catch (IOException e) {
            log.warn("IOException when importFromCsv called");
            model.addAttribute("errors", Collections.singletonList("Error during import. Try again"));
        } catch (HttpClientErrorException exception) {
            ErrorDto errorDto = exception.getResponseBodyAs(ErrorDto.class);
            log.warn(errorDto.getMessages().toString());
            model.addAttribute("errors", errorDto.getMessages());
        }
        return "tables/skuGroups/skuGroups";
    }
}
