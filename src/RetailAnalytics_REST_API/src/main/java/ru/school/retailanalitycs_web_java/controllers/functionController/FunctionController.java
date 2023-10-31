package ru.school.retailanalitycs_web_java.controllers.functionController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.school.retailanalitycs_web_java.controllers.functionController.requestEntity.FrequencyOfVisitRequest;
import ru.school.retailanalitycs_web_java.controllers.functionController.requestEntity.MarginGrowthOfferRequest;
import ru.school.retailanalitycs_web_java.controllers.functionController.requestEntity.PersonalOfferByDatesRequest;
import ru.school.retailanalitycs_web_java.controllers.functionController.requestEntity.PersonalOfferByTransactionsNumberRequest;
import ru.school.retailanalitycs_web_java.dto.functionResultDto.FrequencyOfVisitDto;
import ru.school.retailanalitycs_web_java.dto.functionResultDto.MarginGrowthOfferDto;
import ru.school.retailanalitycs_web_java.dto.functionResultDto.PersonalOfferDto;
import ru.school.retailanalitycs_web_java.mapper.FrequencyOfVisitMapper;
import ru.school.retailanalitycs_web_java.mapper.MarginGrowthOfferMapper;
import ru.school.retailanalitycs_web_java.mapper.PersonalOfferMapper;
import ru.school.retailanalitycs_web_java.services.FunctionsService;
import ru.school.retailanalitycs_web_java.services.MarginGroupOfferService;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("api/v1/functions")
@RequiredArgsConstructor
public class FunctionController {
    private final FunctionsService functionsService;
    private final FrequencyOfVisitMapper frequencyOfVisitMapper;
    private final PersonalOfferMapper personalOfferMapper;
    private final MarginGrowthOfferMapper marginGrowthOfferMapper;
    private final MarginGroupOfferService marginGroupOfferService;

    @GetMapping("update")
    @ResponseStatus(OK)
    public void updateAnalysisFormation() {
        functionsService.updateAnalysisFormation();
    }

    @PostMapping("frequency-of-visits")
    public List<FrequencyOfVisitDto> getFrequencyOfVisit(@Valid @RequestBody FrequencyOfVisitRequest request) {
        return functionsService.getFrequencyOfVisit(request).stream().map(frequencyOfVisitMapper::toDto).toList();
    }

    @PostMapping("personal-offer-growth-by-dates")
    public List<PersonalOfferDto> getPersonalOfferByDates(@Valid @RequestBody PersonalOfferByDatesRequest request) {
        return functionsService.getPersonalOfferGrowthOfTheAverageCheck(request).stream().map(personalOfferMapper::toDto).toList();
    }

    @PostMapping("personal-offer-growth-by-transactions-number")
    public List<PersonalOfferDto> getPersonalOfferByTransactionsNumber(@Valid @RequestBody PersonalOfferByTransactionsNumberRequest request) {
        return functionsService.getPersonalOfferGrowthOfTheAverageCheck(request).stream().map(personalOfferMapper::toDto).toList();
    }

    @PostMapping("margin-growth-offer")
    public List<MarginGrowthOfferDto> getMarginGrowthOffer(@Valid @RequestBody MarginGrowthOfferRequest request) {
        return marginGroupOfferService.getMarginGroup(request).stream().map(marginGrowthOfferMapper::toDto).toList();
//        return functionsService.getMarginGrowthOffer(request).stream().map(marginGrowthOfferMapper::toDto).toList();
    }
}
