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
import ru.s21school.retailanalytics_web.dto.entityDto.checkDto.CheckCreateDto;
import ru.s21school.retailanalytics_web.dto.entityDto.checkDto.CheckPageDto;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Optional;

@Controller
@RequestMapping("data/checks")
@Slf4j
public class CheckController {
    private static final String CHECKS_API_URL = "http://localhost:8081/api/v1/checks";

    private final RestTemplate restTemplate;
    private final ImportExportHandler importExportHandler;

    public CheckController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.importExportHandler = new ImportExportHandler(CHECKS_API_URL, "checks", restTemplate);
    }

    @GetMapping
    public String getChecksPage(@RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "30") int size,
                                Model model) {
        ResponseEntity<CheckPageDto> response =
                restTemplate.exchange(CHECKS_API_URL + String.format("?page=%d&size=%d", page, size),
                        HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                        });
        CheckPageDto checksPage = Optional.ofNullable(response.getBody()).orElse(new CheckPageDto());
        model.addAttribute("entities", checksPage.getContent());
        model.addAttribute("totalPages", checksPage.getTotalPages());
        model.addAttribute("totalElements", checksPage.getTotalElements());
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        return "tables/checks/checks";
    }

    @GetMapping("new")
    public String getCreateCheckPage(Model model) {
        model.addAttribute("check", new CheckCreateDto());
        return "tables/checks/new";
    }

    @PostMapping
    public String createCheck(@Valid @ModelAttribute("check") CheckCreateDto check, Model model) {
        try {
            HttpEntity<CheckCreateDto> httpEntity = new HttpEntity<>(check);
            ResponseEntity<LinkedHashMap<String, Object>> response =
                    restTemplate.exchange(CHECKS_API_URL,
                            HttpMethod.POST, httpEntity, new ParameterizedTypeReference<>() {
                            });
            LinkedHashMap<String, Object> map = response.getBody();
            log.info("New check was saved. Id: {}", map.get("id"));
            return "redirect:/data/checks";
        } catch (HttpClientErrorException exception) {
            ErrorDto errorDto = exception.getResponseBodyAs(ErrorDto.class);
            log.warn(errorDto.getMessages().toString());
            model.addAttribute("errors", errorDto.getMessages());
            return "tables/checks/new";
        }
    }

    @DeleteMapping("/{trId}/{skuId}")
    public String delete(@PathVariable Long trId,
                         @PathVariable Long skuId) {
        restTemplate.delete(CHECKS_API_URL + String.format("/%d/%d", trId, skuId));
        return "redirect:/data/checks";
    }

    @GetMapping("/export")
    public void exportToCsv(HttpServletResponse servletResponse) throws IOException {
        importExportHandler.exportToCsv(servletResponse);
    }

    @PostMapping("/import")
    public String importFromCsv(@RequestParam MultipartFile file) {
        importExportHandler.importFromCsv(file);
        return "redirect:/data/checks";
    }
}
