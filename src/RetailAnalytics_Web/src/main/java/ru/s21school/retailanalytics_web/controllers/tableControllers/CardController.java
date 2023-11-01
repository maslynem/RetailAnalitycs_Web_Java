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
import ru.s21school.retailanalytics_web.dto.entityDto.cardDto.CardCreateDto;
import ru.s21school.retailanalytics_web.dto.entityDto.cardDto.CardReadDto;
import ru.s21school.retailanalytics_web.mappers.CardMapper;
import ru.s21school.retailanalytics_web.services.tableServices.CardService;

import java.io.IOException;
import java.util.Collections;

@Controller
@RequestMapping("data/cards")
@Slf4j
public class CardController {
    private final CardService cardService;
    private final CardMapper mapper;


    @Autowired
    public CardController(CardService cardService, CardMapper mapper) {
        this.cardService = cardService;
        this.mapper = mapper;
    }

    @GetMapping
    public String getCardsPage(@RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "30") int size,
                               Model model) {
        PageDto<CardReadDto> cardPage = cardService.performGetPageRequest(page, size);
        model.addAttribute("entities", cardPage.getContent());
        model.addAttribute("totalPages", cardPage.getTotalPages());
        model.addAttribute("totalElements", cardPage.getTotalElements());
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        return "tables/cards/cards";
    }

    @GetMapping("new")
    public String getCreateCardPage(Model model) {
        model.addAttribute("card", new CardCreateDto());
        return "tables/cards/new";
    }

    @GetMapping("{id}")
    public String getUpdateCardPage(@PathVariable Long id, Model model) {
        CardReadDto cardReadDto = cardService.performFindByIdRequest(id);
        model.addAttribute("cardCreateDto", mapper.map(cardReadDto));
        return "tables/cards/update";
    }

    @PutMapping("{id}")
    public String updateCard(@PathVariable Long id,
                             @ModelAttribute("cardCreateDto") CardCreateDto card,
                             Model model) {
        try {
            cardService.performUpdateCardRequest(id, card);
            return "redirect:/data/cards";
        } catch (HttpClientErrorException exception) {
            ErrorDto errorDto = exception.getResponseBodyAs(ErrorDto.class);
            log.warn(errorDto.getMessages().toString());
            model.addAttribute("errors", errorDto.getMessages());
            return "tables/cards/update";
        }
    }

    @PostMapping
    public String createCard(@Valid @ModelAttribute("card") CardCreateDto card, Model model) {
        try {
            cardService.performSaveCardRequest(card);
            return "redirect:/data/cards";
        } catch (HttpClientErrorException exception) {
            ErrorDto errorDto = exception.getResponseBodyAs(ErrorDto.class);
            log.warn(errorDto.getMessages().toString());
            model.addAttribute("errors", errorDto.getMessages());
            return "tables/cards/new";
        }
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        cardService.performDeleteByIdRequest(id);
        return "redirect:/data/cards";
    }

    @GetMapping("/export")
    public void exportToCsv(HttpServletResponse servletResponse) throws IOException {
        cardService.performExportToCsv(servletResponse);
    }

    @PostMapping("/import")
    public String importFromCsv(@RequestParam MultipartFile file, Model model) {
        try {
            cardService.performImportFromCsv(file.getInputStream());
            return "redirect:/data/cards";
        } catch (IOException e) {
            log.warn("IOException when importFromCsv called");
            model.addAttribute("errors", Collections.singletonList("Error during import. Try again"));
        } catch (HttpClientErrorException exception) {
            ErrorDto errorDto = exception.getResponseBodyAs(ErrorDto.class);
            log.warn(errorDto.getMessages().toString());
            model.addAttribute("errors", errorDto.getMessages());
        }
        return "tables/cards/cards";
    }
}
