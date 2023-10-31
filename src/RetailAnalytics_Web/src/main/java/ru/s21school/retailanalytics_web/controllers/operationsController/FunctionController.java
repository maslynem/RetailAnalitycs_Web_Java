package ru.s21school.retailanalytics_web.controllers.operationsController;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.s21school.retailanalytics_web.controllers.operationsController.requestEntity.FrequencyOfVisitRequest;
import ru.s21school.retailanalytics_web.controllers.operationsController.requestEntity.MarginGrowthOfferRequest;
import ru.s21school.retailanalytics_web.controllers.operationsController.requestEntity.PersonalOfferByDatesRequest;
import ru.s21school.retailanalytics_web.controllers.operationsController.requestEntity.PersonalOfferByTransactionsNumberRequest;
import ru.s21school.retailanalytics_web.dto.functionResultDto.FrequencyOfVisitDto;
import ru.s21school.retailanalytics_web.dto.functionResultDto.MarginGrowthOfferDto;
import ru.s21school.retailanalytics_web.dto.functionResultDto.PersonalOfferDto;
import ru.s21school.retailanalytics_web.services.FunctionsService;

import java.util.List;

@Controller
@RequestMapping("functions")
@RequiredArgsConstructor
public class FunctionController {

    private final FunctionsService functionsService;

    @GetMapping
    public String getFunctionsPage() {
        return "functions/functions";
    }

    @GetMapping("frequency-of-visits")
    public String getFrequencyOfVisitsPage() {
        return "functions/frequencyOfVisits";
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
}
