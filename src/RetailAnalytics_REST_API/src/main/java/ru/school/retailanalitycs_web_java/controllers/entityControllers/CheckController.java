package ru.school.retailanalitycs_web_java.controllers.entityControllers;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.school.retailanalitycs_web_java.dto.entityDto.checkDto.CheckCreateDto;
import ru.school.retailanalitycs_web_java.dto.entityDto.checkDto.CheckReadDto;
import ru.school.retailanalitycs_web_java.entities.tables.Check;
import ru.school.retailanalitycs_web_java.entities.tables.CheckId;
import ru.school.retailanalitycs_web_java.entities.tables.Sku;
import ru.school.retailanalitycs_web_java.entities.tables.Transaction;
import ru.school.retailanalitycs_web_java.exceptions.notFoundExceptions.CheckNotFoundException;
import ru.school.retailanalitycs_web_java.mapper.CheckMapper;
import ru.school.retailanalitycs_web_java.services.entityServices.CheckService;

import java.util.List;

@RestController
@RequestMapping("api/v1/checks")
public class CheckController {
    private final CheckService checkService;
    private final CheckMapper checkMapper;

    public CheckController(CheckService checkService, CheckMapper checkMapper) {
        this.checkService = checkService;
        this.checkMapper = checkMapper;
    }

    @GetMapping
    public List<CheckReadDto> findAllChecks() {
        return checkService.findAll().stream().map(checkMapper::toDto).toList();
    }

    @GetMapping(params = {"page", "size"})
    public Page<CheckReadDto> findAllChecksByPage(@RequestParam("page") int page,
                                                  @RequestParam("size") int size) {
        return checkService.findAllByPage(page, size).map(checkMapper::toDto);
    }

    @GetMapping("/{trId}/{skuId}")
    public CheckReadDto findCheckById(@PathVariable Long trId,
                                      @PathVariable Long skuId) {
        CheckId checkId = getId(trId, skuId);
        return checkService.findById(checkId).map(checkMapper::toDto).orElseThrow(() -> new CheckNotFoundException(checkId));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public CheckReadDto save(@Valid @RequestBody CheckCreateDto checkDto) {
        Check check = checkMapper.toEntity(checkDto);
        Check save = checkService.save(check);
        return checkMapper.toDto(save);
    }

    @PostMapping(value = "all", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void saveAll(@Valid @RequestBody List<CheckCreateDto> checksDto) {
        List<Check> checks = checksDto.stream().map(checkMapper::toEntity).toList();
        checkService.save(checks);
    }

    @PutMapping(value = "/{trId}/{skuId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public CheckReadDto update(@PathVariable Long trId,
                               @PathVariable Long skuId,
                               @Valid @RequestBody CheckCreateDto dto) {
        CheckId checkId = getId(trId, skuId);
        Check check = checkMapper.toEntity(dto);
        Check updated = checkService.update(checkId, check);
        return checkMapper.toDto(updated);
    }

    @DeleteMapping("/{trId}/{skuId}")
    public void delete(@PathVariable Long trId,
                       @PathVariable Long skuId) {
        checkService.deleteById(getId(trId, skuId));
    }

    private CheckId getId(Long trId, Long skuId) {
        return new CheckId(Transaction.builder().id(trId).build(), Sku.builder().id(skuId).build());
    }
}
