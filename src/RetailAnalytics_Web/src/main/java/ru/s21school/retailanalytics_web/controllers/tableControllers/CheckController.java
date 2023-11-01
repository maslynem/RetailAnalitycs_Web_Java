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
import ru.s21school.retailanalytics_web.dto.entityDto.checkDto.CheckCreateDto;
import ru.s21school.retailanalytics_web.dto.entityDto.checkDto.CheckPageDto;
import ru.s21school.retailanalytics_web.dto.entityDto.checkDto.CheckReadDto;
import ru.s21school.retailanalytics_web.mappers.CheckMapper;
import ru.s21school.retailanalytics_web.services.tableServices.CheckService;

import java.io.IOException;
import java.util.Collections;

@Controller
@RequestMapping("data/checks")
@Slf4j
public class CheckController {
    private final CheckService checkService;
    private final CheckMapper mapper;

    public CheckController(CheckService checkService, CheckMapper mapper) {
        this.checkService = checkService;
        this.mapper = mapper;
    }

    @GetMapping
    public String getChecksPage(@RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "30") int size,
                                Model model) {
        CheckPageDto checksPage = checkService.performGetPageRequest(page, size);
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
            checkService.performSaveCheckRequest(check);
            return "redirect:/data/checks";
        } catch (HttpClientErrorException exception) {
            ErrorDto errorDto = exception.getResponseBodyAs(ErrorDto.class);
            log.warn(errorDto.getMessages().toString());
            model.addAttribute("errors", errorDto.getMessages());
            return "tables/checks/new";
        }
    }

    @GetMapping("{trId}/{skuId}")
    public String getUpdateCardPage(@PathVariable Long trId,
                                    @PathVariable Long skuId,
                                    Model model) {
        CheckReadDto checkReadDto = checkService.performFindByIdRequest(trId, skuId);
        model.addAttribute("check", mapper.map(checkReadDto));
        return "tables/checks/update";
    }

    @PutMapping("{trId}/{skuId}")
    public String updateCard(@PathVariable Long trId,
                             @PathVariable Long skuId,
                             @ModelAttribute("check") CheckCreateDto check,
                             Model model) {
        try {
            checkService.performUpdateCheckRequest(trId, skuId, check);
            return "redirect:/data/checks";
        } catch (HttpClientErrorException exception) {
            ErrorDto errorDto = exception.getResponseBodyAs(ErrorDto.class);
            log.warn(errorDto.getMessages().toString());
            model.addAttribute("errors", errorDto.getMessages());
            return "tables/checks/update";
        }
    }


    @DeleteMapping("/{trId}/{skuId}")
    public String delete(@PathVariable Long trId,
                         @PathVariable Long skuId) {
        checkService.performDeleteByIdRequest(trId, skuId);
        return "redirect:/data/checks";
    }

    @GetMapping("/export")
    public void exportToCsv(HttpServletResponse servletResponse) throws IOException {
        checkService.performExportToCsv(servletResponse);
    }

    @PostMapping("/import")
    public String importFromCsv(@RequestParam MultipartFile file, Model model) {
        try {
            checkService.performImportFromCsv(file.getInputStream());
            return "redirect:/data/checks";
        } catch (IOException e) {
            log.warn("IOException when importFromCsv called");
            model.addAttribute("errors", Collections.singletonList("Error during import. Try again"));
        } catch (HttpClientErrorException exception) {
            ErrorDto errorDto = exception.getResponseBodyAs(ErrorDto.class);
            log.warn(errorDto.getMessages().toString());
            model.addAttribute("errors", errorDto.getMessages());
        }
        return "tables/checks/checks";
    }
}
