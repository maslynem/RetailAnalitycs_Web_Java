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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import ru.s21school.retailanalytics_web.dto.ErrorDto;
import ru.s21school.retailanalytics_web.dto.entityDto.skuDto.SkuCreateDto;
import ru.s21school.retailanalytics_web.dto.entityDto.skuDto.SkuPageDto;
import ru.s21school.retailanalytics_web.dto.entityDto.skuDto.SkuReadDto;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Optional;

@Controller
@RequestMapping("data/skus")
@Slf4j
public class SkuController {
    private static final String SKU_API_URL = "http://localhost:8081/api/v1/skus";

    private final RestTemplate restTemplate;
    private final ImportExportHandler importExportHandler;

    public SkuController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.importExportHandler = new ImportExportHandler(SKU_API_URL, "skus", restTemplate);

    }

    @GetMapping
    public String getSkusPage(@RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "30") int size,
                              Model model) {
        ResponseEntity<SkuPageDto> response =
                restTemplate.exchange(SKU_API_URL + String.format("?page=%d&size=%d", page, size),
                        HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                        });
        SkuPageDto skusPage = Optional.ofNullable(response.getBody()).orElse(new SkuPageDto());
        model.addAttribute("entities", skusPage.getContent());
        model.addAttribute("totalPages", skusPage.getTotalPages());
        model.addAttribute("totalElements", skusPage.getTotalElements());
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        return "tables/skus/skus";
    }

    @GetMapping("new")
    public String getCreateSkuPage(Model model) {
        model.addAttribute("sku", new SkuReadDto());
        return "tables/skus/new";
    }

    @PostMapping
    public String createSku(@Valid @ModelAttribute("sku") SkuCreateDto sku, Model model) {
        try {
            HttpEntity<SkuCreateDto> httpEntity = new HttpEntity<>(sku);
            ResponseEntity<LinkedHashMap<String, Object>> response =
                    restTemplate.exchange(SKU_API_URL,
                            HttpMethod.POST, httpEntity, new ParameterizedTypeReference<>() {
                            });
            LinkedHashMap<String, Object> map = response.getBody();
            log.info("New sku was saved. Id: {}", map.get("id"));
            return "redirect:/data/skus";
        } catch (HttpClientErrorException exception) {
            ErrorDto errorDto = exception.getResponseBodyAs(ErrorDto.class);
            log.warn(errorDto.getMessages().toString());
            model.addAttribute("errors", errorDto.getMessages());
            return "tables/skus/new";
        }
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable String id) {
        restTemplate.delete(SKU_API_URL + "/" + id);
        return "redirect:/data/skus";
    }

    @GetMapping("/export")
    public void exportToCsv(HttpServletResponse servletResponse) throws IOException {
        importExportHandler.exportToCsv(servletResponse);
    }

    @PostMapping("/import")
    public String importFromCsv(@RequestParam MultipartFile file) {
        importExportHandler.importFromCsv(file);
        return "redirect:/data/skus";
    }
}
