package ru.school.retailanalitycs_web_java.controllers.entityControllers;

import jakarta.validation.Valid;
import lombok.SneakyThrows;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.school.retailanalitycs_web_java.dto.entityDto.checkDto.CheckCreateDto;
import ru.school.retailanalitycs_web_java.dto.entityDto.checkDto.CheckReadDto;
import ru.school.retailanalitycs_web_java.entities.tables.Check;
import ru.school.retailanalitycs_web_java.entities.tables.CheckId;
import ru.school.retailanalitycs_web_java.exceptions.notFoundExceptions.CheckNotFoundException;
import ru.school.retailanalitycs_web_java.mapper.CheckMapper;
import ru.school.retailanalitycs_web_java.services.entityServices.CheckService;
import ru.school.retailanalitycs_web_java.utils.CsvReader;

import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("api/v1/checks")
public class CheckController {
    private final CheckService checkService;
    private final CheckMapper checkMapper;
    private final CsvReader<CheckCreateDto> csvReader;

    public CheckController(CheckService checkService, CheckMapper checkMapper, CsvReader<CheckCreateDto> csvReader) {
        this.checkService = checkService;
        this.checkMapper = checkMapper;
        this.csvReader = csvReader;
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
    public CheckReadDto create(@Valid @RequestBody CheckCreateDto checkDto) {
        Check check = checkMapper.toEntity(checkDto);
        Check save = checkService.save(check);
        return checkMapper.toDto(save);
    }

    @DeleteMapping("/{trId}/{skuId}")
    public void delete(@PathVariable Long trId,
                       @PathVariable Long skuId) {
        checkService.deleteById(getId(trId, skuId));
    }

    @PostMapping(value = "import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @SneakyThrows
    public void importFromCsv(@RequestPart MultipartFile file) {
        InputStream inputStream = file.getInputStream();
        List<Check> checks = csvReader.importCsv(inputStream, CheckCreateDto.class).stream().map(checkMapper::toEntity).toList();
        checkService.save(checks);
    }

    private CheckId getId(Long trId, Long skuId) {
        return new CheckId(trId, skuId);
    }
}
