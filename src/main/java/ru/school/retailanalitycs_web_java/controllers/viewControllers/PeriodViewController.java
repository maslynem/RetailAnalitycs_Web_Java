package ru.school.retailanalitycs_web_java.controllers.viewControllers;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import ru.school.retailanalitycs_web_java.dto.viewDto.PeriodViewDto;
import ru.school.retailanalitycs_web_java.entities.views.PeriodViewId;
import ru.school.retailanalitycs_web_java.exceptions.notFoundExceptions.PeriodViewNotFoundException;
import ru.school.retailanalitycs_web_java.mapper.PeriodViewMapper;
import ru.school.retailanalitycs_web_java.services.viewServices.PeriodViewService;

import java.util.List;

@RestController
@RequestMapping("api/v1/views/periods")
public class PeriodViewController {
    private final PeriodViewService periodViewService;
    private final PeriodViewMapper periodViewMapper;

    public PeriodViewController(PeriodViewService periodViewService, PeriodViewMapper periodViewMapper) {
        this.periodViewService = periodViewService;
        this.periodViewMapper = periodViewMapper;
    }

    @GetMapping
    public List<PeriodViewDto> findAllPeriods() {
        return periodViewService.findAll().stream().map(periodViewMapper::toDto).toList();
    }

    @GetMapping(params = {"page", "size"})
    public Page<PeriodViewDto> findAllPeriodsByPage(@RequestParam("page") int page,
                                                    @RequestParam("size") int size) {
        return periodViewService.findAllByPage(page, size).map(periodViewMapper::toDto);
    }

    @GetMapping("/{customerId}/{groupId}")
    public PeriodViewDto findPeriodById(@PathVariable Long customerId,
                                        @PathVariable Long groupId) {
        PeriodViewId id = new PeriodViewId(customerId, groupId);
        return periodViewService.findById(id).map(periodViewMapper::toDto).orElseThrow(() -> new PeriodViewNotFoundException(id));
    }

}
