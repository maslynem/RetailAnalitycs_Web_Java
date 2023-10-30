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
import ru.s21school.retailanalytics_web.dto.entityDto.cardDto.CardReadDto;
import ru.s21school.retailanalytics_web.dto.entityDto.checkDto.CheckCreateDto;
import ru.s21school.retailanalytics_web.dto.entityDto.checkDto.CheckPageDto;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Optional;

@Controller
@RequestMapping("data/checks")
@RequiredArgsConstructor
@Slf4j
public class CheckController {
    private static final String TRANSACTION_API_URL = "http://localhost:8081/api/v1/checks";

    private final RestTemplate restTemplate;

    @GetMapping
    public String getChecksPage(@RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "30") int size,
                                Model model) {
        ResponseEntity<CheckPageDto> response =
                restTemplate.exchange(TRANSACTION_API_URL + String.format("?page=%d&size=%d", page, size),
                        HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                        });
        CheckPageDto checksPage = Optional.ofNullable(response.getBody()).orElse(new CheckPageDto());
        model.addAttribute("entities", checksPage.getContent());
        model.addAttribute("totalPages", checksPage.getTotalPages());
        model.addAttribute("totalElements", checksPage.getTotalElements());
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        return "checks/checks";
    }

    @GetMapping("new")
    public String getCreateCheckPage(Model model) {
        model.addAttribute("check", new CheckCreateDto());
        return "checks/new";
    }

    @PostMapping
    public String createCheck(@Valid @ModelAttribute("check") CheckCreateDto check, BindingResult bindingResult, Model model) {
        try {
            HttpEntity<CheckCreateDto> httpEntity = new HttpEntity<>(check);
            ResponseEntity<LinkedHashMap<String, Object>> response =
                    restTemplate.exchange(TRANSACTION_API_URL,
                            HttpMethod.POST, httpEntity, new ParameterizedTypeReference<>() {
                            });
            LinkedHashMap<String, Object> map = response.getBody();
            log.info("New check was saved. Id: {}", map.get("id"));
            return "redirect:/data/checks";
        } catch (HttpClientErrorException exception) {
            ErrorDto errorDto = exception.getResponseBodyAs(ErrorDto.class);
            log.warn(errorDto.getMessages().toString());
            model.addAttribute("errors", errorDto.getMessages());
            return "checks/new";
        }
    }


    @DeleteMapping("/{trId}/{skuId}")
    public String delete(@PathVariable Long trId,
                         @PathVariable Long skuId) {
        restTemplate.delete(TRANSACTION_API_URL + String.format("/%d/%d", trId, skuId));
        return "redirect:/data/checks";
    }

    @GetMapping("/export")
    public void exportToCsv(HttpServletResponse servletResponse) throws IOException {
        Resource resource = restTemplate.getForObject(TRANSACTION_API_URL + "/export", Resource.class);
        servletResponse.setContentType("text/csv");
        servletResponse.addHeader("Content-Disposition", "attachment; filename=\"checks.tsv\"");
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
        String url = TRANSACTION_API_URL + "/import";
        restTemplate.postForEntity(url, httpEntity, Object.class);
        return "redirect:/data/checks";
    }
}
