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
import ru.s21school.retailanalytics_web.dto.entityDto.storeDto.StoreCreateDto;
import ru.s21school.retailanalytics_web.dto.entityDto.storeDto.StorePageDto;
import ru.s21school.retailanalytics_web.dto.entityDto.storeDto.StoreReadDto;
import ru.s21school.retailanalytics_web.mappers.StoreMapper;
import ru.s21school.retailanalytics_web.services.tableServices.StoreService;

import java.io.IOException;

@Controller
@RequestMapping("data/stores")
@Slf4j
public class StoreController {
    private final StoreService storeService;
    private final StoreMapper mapper;

    @Autowired
    public StoreController(StoreService storeService, StoreMapper mapper) {
        this.storeService = storeService;
        this.mapper = mapper;
    }

    @GetMapping
    public String getStoresPage(@RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "30") int size,
                                Model model) {
        StorePageDto storesPage = storeService.performGetPageRequest(page, size);
        model.addAttribute("entities", storesPage.getContent());
        model.addAttribute("totalPages", storesPage.getTotalPages());
        model.addAttribute("totalElements", storesPage.getTotalElements());
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        return "tables/stores/stores";
    }

    @GetMapping("new")
    public String getCreateStorePage(Model model) {
        model.addAttribute("store", new StoreCreateDto());
        return "tables/stores/new";
    }

    @PostMapping
    public String createStore(@Valid @ModelAttribute("store") StoreCreateDto store, Model model) {
        try {
            storeService.performSaveStoreRequest(store);
            return "redirect:/data/stores";
        } catch (HttpClientErrorException exception) {
            ErrorDto errorDto = exception.getResponseBodyAs(ErrorDto.class);
            log.warn(errorDto.getMessages().toString());
            model.addAttribute("errors", errorDto.getMessages());
            return "tables/stores/new";
        }
    }

    @GetMapping("{trStoreId}/{skuId}")
    public String getUpdateCardPage(@PathVariable Long trStoreId,
                                    @PathVariable Long skuId,
                                    Model model) {
        StoreReadDto storeReadDto = storeService.performFindByIdRequest(trStoreId, skuId);
        model.addAttribute("store", mapper.map(storeReadDto));
        return "tables/stores/update";
    }

    @PutMapping("{trStoreId}/{skuId}")
    public String updateCard(@PathVariable Long trStoreId,
                             @PathVariable Long skuId,
                             @ModelAttribute("store") StoreCreateDto store,
                             Model model) {
        try {
            storeService.performUpdateStoreRequest(trStoreId, skuId, store);
            return "redirect:/data/stores";
        } catch (HttpClientErrorException exception) {
            ErrorDto errorDto = exception.getResponseBodyAs(ErrorDto.class);
            log.warn(errorDto.getMessages().toString());
            model.addAttribute("errors", errorDto.getMessages());
            return "tables/stores/update";
        }
    }
    
    @DeleteMapping("/{trStoreId}/{skuId}")
    public String delete(@PathVariable Long trStoreId,
                         @PathVariable Long skuId) {
        storeService.performDeleteByIdRequest(trStoreId, skuId);

        return "redirect:/data/stores";
    }

    @GetMapping("/export")
    public void exportToCsv(HttpServletResponse servletResponse) throws IOException {
        storeService.performExportToCsv(servletResponse);

    }

    @PostMapping("/import")
    public String importFromCsv(@RequestParam MultipartFile file) {
        storeService.performImportFromCsv(file);
        return "redirect:/data/stores";
    }
}
