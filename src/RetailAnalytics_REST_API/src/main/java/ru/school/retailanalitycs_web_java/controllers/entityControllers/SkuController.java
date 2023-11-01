package ru.school.retailanalitycs_web_java.controllers.entityControllers;

import jakarta.validation.Valid;
import lombok.SneakyThrows;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.school.retailanalitycs_web_java.dto.entityDto.skuDto.SkuCreateDto;
import ru.school.retailanalitycs_web_java.dto.entityDto.skuDto.SkuReadDto;
import ru.school.retailanalitycs_web_java.entities.tables.Sku;
import ru.school.retailanalitycs_web_java.exceptions.notFoundExceptions.SkuNotFoundException;
import ru.school.retailanalitycs_web_java.mapper.SkuMapper;
import ru.school.retailanalitycs_web_java.services.entityServices.SkuService;
import ru.school.retailanalitycs_web_java.utils.CsvReader;
import ru.school.retailanalitycs_web_java.utils.CsvWriter;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("api/v1/skus")
public class SkuController {
    private final SkuService skuService;
    private final SkuMapper skuMapper;
    private final CsvReader<SkuCreateDto> csvReader;
    private final CsvWriter<SkuCreateDto> csvWriter;

    public SkuController(SkuService skuService, SkuMapper skuMapper, CsvReader<SkuCreateDto> csvReader, CsvWriter<SkuCreateDto> csvWriter) {
        this.skuService = skuService;
        this.skuMapper = skuMapper;
        this.csvReader = csvReader;
        this.csvWriter = csvWriter;
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
    public SkuReadDto findSkuById(@PathVariable Long id) {
        return skuService.findById(id).map(skuMapper::toDto).orElseThrow(() -> new SkuNotFoundException(id));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public SkuReadDto create(@Valid @RequestBody SkuCreateDto skuDto) {
        Sku sku = skuMapper.toEntity(skuDto);
        Sku save = skuService.save(sku);
        return skuMapper.toDto(save);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public SkuReadDto update(@PathVariable Long id, @Valid @RequestBody SkuCreateDto skuDto) {
        Sku sku = skuMapper.toEntity(skuDto);
        Sku updated = skuService.update(id, sku);
        return skuMapper.toDto(updated);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        skuService.deleteById(id);
    }

    @PostMapping(value = "import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @SneakyThrows
    public void importFromCsv(@RequestPart MultipartFile file) {
        InputStream inputStream = file.getInputStream();
        List<Sku> skus = csvReader.importCsv(inputStream, SkuCreateDto.class).stream().map(skuMapper::toEntity).toList();
        skuService.save(skus);
    }

    @GetMapping(value = "export")
    @SneakyThrows
    public ResponseEntity<Resource> exportToCsv() {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        List<SkuCreateDto> customers = skuService.findAll().stream().map(skuMapper::toCreateDto).toList();
        csvWriter.exportCsv(os, customers, SkuCreateDto.class);
        ByteArrayResource res = new ByteArrayResource(os.toByteArray());
        return ResponseEntity.ok(res);
    }
}
