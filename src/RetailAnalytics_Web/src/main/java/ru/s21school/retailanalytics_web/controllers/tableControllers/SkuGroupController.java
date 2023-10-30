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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import ru.s21school.retailanalytics_web.dto.ErrorDto;
import ru.s21school.retailanalytics_web.dto.entityDto.skuGroupDto.SkuGroupDto;
import ru.s21school.retailanalytics_web.dto.entityDto.skuGroupDto.SkuGroupPageDto;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Optional;

@Controller
@RequestMapping("data/sku-groups")
@Slf4j
public class SkuGroupController {
    private static final String SKU_GROUP_API_URL = "http://localhost:8081/api/v1/sku-groups";

    private final RestTemplate restTemplate;
    private final ImportExportHandler importExportHandler;

    public SkuGroupController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.importExportHandler = new ImportExportHandler(SKU_GROUP_API_URL, "sku-groups", restTemplate);

    }

    @GetMapping
    public String getSkuGroupsPage(@RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "30") int size,
                                   Model model) {
        ResponseEntity<SkuGroupPageDto> response =
                restTemplate.exchange(SKU_GROUP_API_URL + String.format("?page=%d&size=%d", page, size),
                        HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                        });
        SkuGroupPageDto skuGroupsPage = Optional.ofNullable(response.getBody()).orElse(new SkuGroupPageDto());
        model.addAttribute("entities", skuGroupsPage.getContent());
        model.addAttribute("totalPages", skuGroupsPage.getTotalPages());
        model.addAttribute("totalElements", skuGroupsPage.getTotalElements());
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
            HttpEntity<SkuGroupDto> httpEntity = new HttpEntity<>(skuGroup);
            ResponseEntity<LinkedHashMap<String, Object>> response =
                    restTemplate.exchange(SKU_GROUP_API_URL,
                            HttpMethod.POST, httpEntity, new ParameterizedTypeReference<>() {
                            });
            LinkedHashMap<String, Object> map = response.getBody();
            log.info("New skuGroup was saved. Id: {}", map.get("id"));
            return "redirect:/data/skuGroups";
        } catch (HttpClientErrorException exception) {
            ErrorDto errorDto = exception.getResponseBodyAs(ErrorDto.class);
            log.warn(errorDto.getMessages().toString());
            model.addAttribute("errors", errorDto.getMessages());
            return "tables/skuGroups/new";
        }
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable String id) {
        restTemplate.delete(SKU_GROUP_API_URL + "/" + id);
        return "redirect:/data/skuGroups";
    }

    @GetMapping("/export")
    public void exportToCsv(HttpServletResponse servletResponse) throws IOException {
        importExportHandler.exportToCsv(servletResponse);
    }

    @PostMapping("/import")
    public String importFromCsv(@RequestParam MultipartFile file) {
        importExportHandler.importFromCsv(file);
        return "redirect:/data/skuGroups";
    }
}
