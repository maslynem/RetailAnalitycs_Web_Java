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
import ru.s21school.retailanalytics_web.dto.PageDto;
import ru.s21school.retailanalytics_web.dto.entityDto.skuDto.SkuCreateDto;
import ru.s21school.retailanalytics_web.dto.entityDto.skuDto.SkuReadDto;
import ru.s21school.retailanalytics_web.mappers.SkuMapper;
import ru.s21school.retailanalytics_web.services.tableServices.SkuService;

import java.io.IOException;
import java.util.Collections;

@Controller
@RequestMapping("data/skus")
@Slf4j
public class SkuController {
    private final SkuService skuService;
    private final SkuMapper mapper;

    @Autowired
    public SkuController(SkuService skuService, SkuMapper mapper) {
        this.skuService = skuService;
        this.mapper = mapper;
    }

    @GetMapping
    public String getSkusPage(@RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "30") int size,
                              Model model) {
        PageDto<SkuReadDto> skuPage = skuService.performGetPageRequest(page, size);
        model.addAttribute("entities", skuPage.getContent());
        model.addAttribute("totalPages", skuPage.getTotalPages());
        model.addAttribute("totalElements", skuPage.getTotalElements());
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        return "tables/skus/skus";
    }

    @GetMapping("new")
    public String getCreateSkuPage(Model model) {
        model.addAttribute("sku", new SkuCreateDto());
        return "tables/skus/new";
    }

    @GetMapping("{id}")
    public String getUpdateSkuPage(@PathVariable Long id, Model model) {
        SkuReadDto skuReadDto = skuService.performFindByIdRequest(id);
        model.addAttribute("sku", mapper.map(skuReadDto));
        return "tables/skus/update";
    }

    @PutMapping("{id}")
    public String updateSku(@PathVariable Long id,
                            @ModelAttribute("sku") SkuCreateDto sku,
                            Model model) {
        try {
            skuService.performUpdateSkuRequest(id, sku);
            return "redirect:/data/skus";
        } catch (HttpClientErrorException exception) {
            ErrorDto errorDto = exception.getResponseBodyAs(ErrorDto.class);
            log.warn(errorDto.getMessages().toString());
            model.addAttribute("errors", errorDto.getMessages());
            return "tables/skus/update";
        }
    }
    
    @PostMapping
    public String createSku(@Valid @ModelAttribute("sku") SkuCreateDto sku, Model model) {
        try {
            skuService.performSaveSkuRequest(sku);
            return "redirect:/data/skus";
        } catch (HttpClientErrorException exception) {
            ErrorDto errorDto = exception.getResponseBodyAs(ErrorDto.class);
            log.warn(errorDto.getMessages().toString());
            model.addAttribute("errors", errorDto.getMessages());
            return "tables/skus/new";
        }
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        skuService.performDeleteByIdRequest(id);
        return "redirect:/data/skus";
    }

    @GetMapping("/export")
    public void exportToCsv(HttpServletResponse servletResponse) throws IOException {
        skuService.performExportToCsv(servletResponse);
    }

    @PostMapping("/import")
    public String importFromCsv(@RequestParam MultipartFile file, Model model) {
        try {
            skuService.performImportFromCsv(file.getInputStream());
            return "redirect:/data/skus";
        } catch (IOException e) {
            log.warn("IOException when importFromCsv called");
            model.addAttribute("errors", Collections.singletonList("Error during import. Try again"));
        } catch (HttpClientErrorException exception) {
            ErrorDto errorDto = exception.getResponseBodyAs(ErrorDto.class);
            log.warn(errorDto.getMessages().toString());
            model.addAttribute("errors", errorDto.getMessages());
        }
        return "tables/skus/skus";
    }
}
