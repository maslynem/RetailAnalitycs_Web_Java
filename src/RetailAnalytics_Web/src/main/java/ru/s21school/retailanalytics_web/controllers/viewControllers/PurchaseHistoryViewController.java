package ru.s21school.retailanalytics_web.controllers.viewControllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import ru.s21school.retailanalytics_web.dto.entityDto.PageDto;
import ru.s21school.retailanalytics_web.dto.viewDto.PurchaseHistoryViewDto;

import java.util.Optional;

@Controller
@RequestMapping("views/purchase-histories")
@RequiredArgsConstructor
@Slf4j
public class PurchaseHistoryViewController {
    private static final String PH_API_URL = "http://localhost:8081/api/v1/views/purchase-histories";

    private final RestTemplate restTemplate;

    @GetMapping
    public String getPurchaseHistoryViewPage(@RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "30") int size,
                                             Model model) {
        ResponseEntity<PageDto<PurchaseHistoryViewDto>> response =
                restTemplate.exchange(PH_API_URL + String.format("?page=%d&size=%d", page, size),
                        HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                        });
        PageDto<PurchaseHistoryViewDto> customersPage = Optional.ofNullable(response.getBody()).orElseThrow();
        model.addAttribute("entities", customersPage.getContent());
        model.addAttribute("totalPages", customersPage.getTotalPages());
        model.addAttribute("totalElements", customersPage.getTotalElements());
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        return "views/purchaseHistory";
    }
}
