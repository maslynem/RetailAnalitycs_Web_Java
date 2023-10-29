package ru.s21school.retailanalytics_web.controllers;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import ru.s21school.retailanalytics_web.dto.ErrorDto;
import ru.s21school.retailanalytics_web.dto.entityDto.cardDto.CardCreateDto;
import ru.s21school.retailanalytics_web.dto.entityDto.cardDto.CardPageDto;
import ru.s21school.retailanalytics_web.dto.entityDto.cardDto.CardReadDto;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Optional;

@Controller
@RequestMapping("data/cards")
@RequiredArgsConstructor
@Slf4j
public class CardController {
    private static final String CARD_API_URL = "http://localhost:8081/api/v1/cards";

    private final RestTemplate restTemplate;

    @GetMapping
    public String getCardsPage(@RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "30") int size,
                               Model model) {
        ResponseEntity<CardPageDto> response =
                restTemplate.exchange(CARD_API_URL + String.format("?page=%d&size=%d", page, size),
                        HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                        });
        CardPageDto cardsPage = Optional.ofNullable(response.getBody()).orElse(new CardPageDto());
        model.addAttribute("entities", cardsPage.getContent());
        model.addAttribute("totalPages", cardsPage.getTotalPages());
        model.addAttribute("totalElements", cardsPage.getTotalElements());
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        return "cards/cards";
    }

    @GetMapping("new")
    public String getCreateCardPage(Model model) {
        model.addAttribute("card", new CardReadDto());
        return "cards/new";
    }

    @PostMapping
    public String createCard(@Valid @ModelAttribute("card") CardCreateDto card, BindingResult bindingResult, Model model) {
        try {
            HttpEntity<CardCreateDto> httpEntity = new HttpEntity<>(card);
            ResponseEntity<LinkedHashMap<String, Object>> response =
                    restTemplate.exchange(CARD_API_URL,
                            HttpMethod.POST, httpEntity, new ParameterizedTypeReference<>() {
                            });
            LinkedHashMap<String, Object> map = response.getBody();
            log.info("New card was saved. Id: {}", map.get("id"));
            return "redirect:/data/cards";
        } catch (HttpClientErrorException exception) {
            ErrorDto errorDto = exception.getResponseBodyAs(ErrorDto.class);
            log.warn(errorDto.getMessages().toString());
            model.addAttribute("errors", errorDto.getMessages());
            return "cards/new";
        }
    }


    @DeleteMapping("/{id}")
    public String delete(@PathVariable String id) {
        restTemplate.delete(CARD_API_URL + "/" + id);
        return "redirect:/data/cards";
    }

    @GetMapping("/export")
    public void exportToCsv(HttpServletResponse servletResponse) throws IOException {
        Resource resource = restTemplate.getForObject(CARD_API_URL + "/export", Resource.class);
        servletResponse.setContentType("text/csv");
        servletResponse.addHeader("Content-Disposition", "attachment; filename=\"cards.tsv\"");
        servletResponse.setCharacterEncoding("UTF-8");
        servletResponse.getWriter().print(resource.getContentAsString(StandardCharsets.UTF_8));
    }

    @PostMapping("/import")
    public String importFromCsv(@RequestParam MultipartFile file) {
        Resource invoicesResource = file.getResource();
        LinkedMultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();
        parts.add("file", invoicesResource);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<LinkedMultiValueMap<String, Object>> httpEntity = new HttpEntity<>(parts, httpHeaders);
        String url = CARD_API_URL + "/import";
        restTemplate.postForEntity(url, httpEntity, Object.class);
        return "redirect:/data/cards";
    }
}
