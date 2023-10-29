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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import ru.s21school.retailanalytics_web.dto.ErrorDto;
import ru.s21school.retailanalytics_web.dto.entityDto.skuGroupDto.SkuGroupDto;
import ru.s21school.retailanalytics_web.dto.entityDto.skuGroupDto.SkuGroupPageDto;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Optional;

@Controller
@RequestMapping("data/sku-groups")
@RequiredArgsConstructor
@Slf4j
public class SkuGroupController {
    private static final String SKU_GROUP_API_URL = "http://localhost:8081/api/v1/sku-groups";

    private final RestTemplate restTemplate;

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
        return "skuGroups/skuGroups";
    }

    @GetMapping("new")
    public String getCreateSkuGroupPage(Model model) {
        model.addAttribute("skuGroup", new SkuGroupDto());
        return "skuGroups/new";
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
            return "skuGroups/new";
        }
    }


    @DeleteMapping("/{id}")
    public String delete(@PathVariable String id) {
        restTemplate.delete(SKU_GROUP_API_URL + "/" + id);
        return "redirect:/data/skuGroups";
    }

    @GetMapping("/export")
    public void exportToCsv(HttpServletResponse servletResponse) throws IOException {
        Resource resource = restTemplate.getForObject(SKU_GROUP_API_URL + "/export", Resource.class);
        servletResponse.setContentType("text/csv");
        servletResponse.addHeader("Content-Disposition", "attachment; filename=\"skuGroups.tsv\"");
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
        String url = SKU_GROUP_API_URL + "/import";
        restTemplate.postForEntity(url, httpEntity, Object.class);
        return "redirect:/data/skuGroups";
    }
}
