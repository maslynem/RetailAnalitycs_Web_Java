package ru.school.retailanalitycs_web_java.controllers;

import jakarta.validation.Valid;
import lombok.SneakyThrows;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.school.retailanalitycs_web_java.dto.skuDto.SkuCreateDto;
import ru.school.retailanalitycs_web_java.dto.skuDto.SkuReadDto;
import ru.school.retailanalitycs_web_java.entities.tables.Sku;
import ru.school.retailanalitycs_web_java.exceptions.notFoundExceptions.SkuNotFoundException;
import ru.school.retailanalitycs_web_java.mapper.SkuMapper;
import ru.school.retailanalitycs_web_java.services.SkuService;
import ru.school.retailanalitycs_web_java.utils.CsvReader;

import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("api/v1/skus")
public class SkuController {
    private final SkuService skuService;
    private final SkuMapper skuMapper;
    private final CsvReader<SkuCreateDto> csvReader;

    public SkuController(SkuService skuService, SkuMapper skuMapper, CsvReader<SkuCreateDto> csvReader) {
        this.skuService = skuService;
        this.skuMapper = skuMapper;
        this.csvReader = csvReader;
    }

    @GetMapping
    public List<SkuReadDto> findAllSkus() {
        return skuService.findAll().stream().map(skuMapper::toDto).toList();
    }

    @GetMapping(params = {"page", "size"})
    public Page<SkuReadDto> findAllSkusByPage(@RequestParam("page") int page,
                                              @RequestParam("size") int size) {
        return skuService.findAllByPage(page, size).map(skuMapper::toDto);
    }

    @GetMapping("/{id}")
    public SkuReadDto findSkuById(@PathVariable Integer id) {
        return skuService.findById(id).map(skuMapper::toDto).orElseThrow(() -> new SkuNotFoundException(id));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public SkuReadDto create(@Valid @RequestBody SkuCreateDto skuDto) {
        Sku sku = skuMapper.toEntity(skuDto);
        Sku save = skuService.save(sku);
        return skuMapper.toDto(save);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Integer id) {
        skuService.deleteById(id);
    }

    @PostMapping(value = "import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @SneakyThrows
    public void importFromCsv(@RequestPart MultipartFile multipartFile) {
        InputStream inputStream = multipartFile.getInputStream();
        List<Sku> skus = csvReader.importCsv(inputStream, SkuCreateDto.class).stream().map(skuMapper::toEntity).toList();
        skuService.save(skus);
    }
}
