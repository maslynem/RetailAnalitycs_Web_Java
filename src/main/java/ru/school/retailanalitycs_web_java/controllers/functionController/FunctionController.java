package ru.school.retailanalitycs_web_java.controllers.functionController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.school.retailanalitycs_web_java.controllers.functionController.requestEntity.FrequencyOfVisitRequest;
import ru.school.retailanalitycs_web_java.dto.functionResultDto.FrequencyOfVisitDto;
import ru.school.retailanalitycs_web_java.mapper.FrequencyOfVisitMapper;
import ru.school.retailanalitycs_web_java.services.FunctionsService;

import java.util.List;

@RestController
@RequestMapping("api/v1/functions")
public class FunctionController {
    @Autowired
    private FunctionsService functionsService;

    @Autowired
    private FrequencyOfVisitMapper frequencyOfVisitMapper;

    @PostMapping("frequency-of-visits")
    List<FrequencyOfVisitDto> getFrequencyOfVisit(@RequestBody FrequencyOfVisitRequest request) {
        return functionsService.getFrequencyOfVisit(
                request.getFirstDate(),
                request.getLastDate(),
                request.getAddedTransactions(),
                request.getMaxChurnIndex(),
                request.getMaxShareOfTransactions(),
                request.getAllowableMarginShare()).stream().map(frequencyOfVisitMapper::toDto).toList();
    }
}
