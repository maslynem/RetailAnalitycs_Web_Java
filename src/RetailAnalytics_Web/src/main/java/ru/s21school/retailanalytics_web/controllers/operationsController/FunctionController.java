package ru.s21school.retailanalytics_web.controllers.operationsController;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import ru.s21school.retailanalytics_web.controllers.operationsController.requestEntity.FrequencyOfVisitRequest;
import ru.s21school.retailanalytics_web.controllers.operationsController.requestEntity.MarginGrowthOfferRequest;
import ru.s21school.retailanalytics_web.controllers.operationsController.requestEntity.PersonalOfferByDatesRequest;
import ru.s21school.retailanalytics_web.controllers.operationsController.requestEntity.PersonalOfferByTransactionsNumberRequest;
import ru.s21school.retailanalytics_web.dto.ErrorDto;
import ru.s21school.retailanalytics_web.dto.functionResultDto.FrequencyOfVisitDto;
import ru.s21school.retailanalytics_web.dto.functionResultDto.MarginGrowthOfferDto;
import ru.s21school.retailanalytics_web.dto.functionResultDto.PersonalOfferDto;
import ru.s21school.retailanalytics_web.services.functionServices.FunctionsService;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("functions")
@RequiredArgsConstructor
@Slf4j
public class FunctionController {

    private final FunctionsService functionsService;

    @ExceptionHandler(HttpClientErrorException.class)
    public String handleSQLGrammarException(HttpClientErrorException exception, Model model) {
        ErrorDto errorDto = exception.getResponseBodyAs(ErrorDto.class);
        log.warn("handle exception: HttpClientErrorException. Code: {}. Message: {}", errorDto.getCode(), errorDto.getMessages().toString());
        model.addAttribute("errors", errorDto.getMessages());
        return "functions/functions";
    }

    @GetMapping
    public String getFunctionsPage() {
        return "functions/functions";
    }

    @PostMapping("update")
    public String updateDataAnalysis(Model model) {
        functionsService.updateDataAnalysis();
        model.addAttribute("updated", "SUCCESS");
        return "functions/functions";
    }

    @GetMapping("frequency-of-visits")
    public String getFrequencyOfVisitsPage() {
        return "functions/frequencyOfVisits";
    }

    @GetMapping("frequency-of-visits/export")
    public void getFrequencyOfVisits(HttpServletResponse servletResponse) throws IOException {
        functionsService.exportFrequencyOfVisit(servletResponse);
    }

    @PostMapping("frequency-of-visits")
    public String getFrequencyOfVisit(@ModelAttribute FrequencyOfVisitRequest request, Model model) {
        List<FrequencyOfVisitDto> frequencyOfVisit = functionsService.getFrequencyOfVisit(request);
        model.addAttribute("entities", frequencyOfVisit);
        return "functions/frequencyOfVisits";
    }

    @GetMapping("personal-offer-growth")
    public String getPersonalOfferGrowsPage() {
        return "functions/personalOfferGrowth";
    }

    @GetMapping("personal-offer-growth/export")
    public void getPersonalOffer(HttpServletResponse servletResponse) throws IOException {
        functionsService.exportPersonalOffer(servletResponse);
    }

    @PostMapping("personal-offer-growth-by-dates")
    public String getPersonalOfferByDates(@ModelAttribute PersonalOfferByDatesRequest request, Model model) {
        List<PersonalOfferDto> frequencyOfVisit = functionsService.getPersonalOfferByDates(request);
        model.addAttribute("entities", frequencyOfVisit);
        return "functions/personalOfferGrowth";
    }

    @PostMapping("personal-offer-growth-by-transactions-number")
    public String getPersonalOfferByTransactionsNumber(@ModelAttribute PersonalOfferByTransactionsNumberRequest request, Model model) {
        List<PersonalOfferDto> frequencyOfVisit = functionsService.getPersonalOfferByTransactionsNumber(request);
        model.addAttribute("entities", frequencyOfVisit);
        return "functions/personalOfferGrowth";
    }


    @GetMapping("margin-growth-offer")
    public String getMarginGrowthOfferPage() {
        return "functions/marginGrowthOffer";
    }

    @PostMapping("margin-growth-offer")
    public String getMarginGrowthOffer(@ModelAttribute MarginGrowthOfferRequest request, Model model) {
        List<MarginGrowthOfferDto> marginGrowthOffer = functionsService.getMarginGrowthOffer(request);
        model.addAttribute("entities", marginGrowthOffer);
        return "functions/marginGrowthOffer";
    }

    @GetMapping("margin-growth-offer/export")
    public void getMarginGrowthOffer(HttpServletResponse servletResponse) throws IOException {
        functionsService.exportMarginGrowthOffer(servletResponse);
    }
}
