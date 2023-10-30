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
import ru.s21school.retailanalytics_web.dto.entityDto.storeDto.StoreCreateDto;
import ru.s21school.retailanalytics_web.dto.entityDto.storeDto.StorePageDto;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Optional;

@Controller
@RequestMapping("data/stores")
@Slf4j
public class StoreController {
    private static final String STORES_API_URL = "http://localhost:8081/api/v1/stores";

    private final RestTemplate restTemplate;
    private final ImportExportHandler importExportHandler;

    public StoreController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.importExportHandler = new ImportExportHandler(STORES_API_URL, "stores", restTemplate);

    }

    @GetMapping
    public String getStoresPage(@RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "30") int size,
                                Model model) {
        ResponseEntity<StorePageDto> response =
                restTemplate.exchange(STORES_API_URL + String.format("?page=%d&size=%d", page, size),
                        HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                        });
        StorePageDto storesPage = Optional.ofNullable(response.getBody()).orElse(new StorePageDto());
        model.addAttribute("entities", storesPage.getContent());
        model.addAttribute("totalPages", storesPage.getTotalPages());
        model.addAttribute("totalElements", storesPage.getTotalElements());
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        return "stores/stores";
    }

    @GetMapping("new")
    public String getCreateStorePage(Model model) {
        model.addAttribute("store", new StoreCreateDto());
        return "stores/new";
    }

    @PostMapping
    public String createStore(@Valid @ModelAttribute("store") StoreCreateDto store, BindingResult bindingResult, Model model) {
        try {
            HttpEntity<StoreCreateDto> httpEntity = new HttpEntity<>(store);
            ResponseEntity<LinkedHashMap<String, Object>> response =
                    restTemplate.exchange(STORES_API_URL,
                            HttpMethod.POST, httpEntity, new ParameterizedTypeReference<>() {
                            });
            LinkedHashMap<String, Object> map = response.getBody();
            log.info("New store was saved. Id: {}", map.get("id"));
            return "redirect:/data/stores";
        } catch (HttpClientErrorException exception) {
            ErrorDto errorDto = exception.getResponseBodyAs(ErrorDto.class);
            log.warn(errorDto.getMessages().toString());
            model.addAttribute("errors", errorDto.getMessages());
            return "stores/new";
        }
    }

    @DeleteMapping("/{trStoreId}/{skuId}")
    public String delete(@PathVariable Long trStoreId,
                         @PathVariable Long skuId) {
        restTemplate.delete(STORES_API_URL + String.format("/%d/%d", trStoreId, skuId));
        return "redirect:/data/stores";
    }

    @GetMapping("/export")
    public void exportToCsv(HttpServletResponse servletResponse) throws IOException {
        importExportHandler.exportToCsv(servletResponse);
    }

    @PostMapping("/import")
    public String importFromCsv(@RequestParam MultipartFile file) {
        importExportHandler.importFromCsv(file);
        return "redirect:/data/stores";
    }
}
