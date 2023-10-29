package ru.school.retailanalitycs_web_java.controllers.entityControllers;

import jakarta.validation.Valid;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.school.retailanalitycs_web_java.dto.entityDto.skuGroupDto.SkuGroupDto;
import ru.school.retailanalitycs_web_java.entities.tables.SkuGroup;
import ru.school.retailanalitycs_web_java.exceptions.notFoundExceptions.SkuGroupNotFoundException;
import ru.school.retailanalitycs_web_java.mapper.SkuGroupMapper;
import ru.school.retailanalitycs_web_java.services.entityServices.SkuGroupService;
import ru.school.retailanalitycs_web_java.utils.CsvReader;
import ru.school.retailanalitycs_web_java.utils.CsvWriter;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("api/v1/sku-groups")
public class SkuGroupController {
    private final SkuGroupService skuGroupService;
    private final SkuGroupMapper skuGroupMapper;
    private final CsvReader<SkuGroupDto> csvReader;
    private final CsvWriter<SkuGroupDto> csvWriter;

    @Autowired
    public SkuGroupController(SkuGroupService skuGroupService, SkuGroupMapper skuGroupMapper, CsvReader<SkuGroupDto> csvReader, CsvWriter<SkuGroupDto> csvWriter) {
        this.skuGroupService = skuGroupService;
        this.skuGroupMapper = skuGroupMapper;
        this.csvReader = csvReader;
        this.csvWriter = csvWriter;
    }

    @GetMapping
    public List<SkuGroupDto> findAllSkuGroups() {
        return skuGroupService.findAll().stream().map(skuGroupMapper::toDto).toList();
    }

    @GetMapping(params = {"page", "size"})
    public Page<SkuGroupDto> findAllSkuGroupsByPage(@RequestParam("page") int page,
                                                    @RequestParam("size") int size) {
        return skuGroupService.findAllByPage(page, size).map(skuGroupMapper::toDto);
    }

    @GetMapping("/{id}")
    public SkuGroupDto findSkuGroupById(@PathVariable Long id) {
        return skuGroupService.findById(id).map(skuGroupMapper::toDto).orElseThrow(() -> new SkuGroupNotFoundException(id));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public SkuGroupDto create(@Valid @RequestBody SkuGroupDto skuGroupDto) {
        SkuGroup skuGroup = skuGroupMapper.toEntity(skuGroupDto);
        SkuGroup save = skuGroupService.save(skuGroup);
        skuGroupDto.setId(save.getId());
        return skuGroupDto;
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        skuGroupService.deleteById(id);
    }

    @PostMapping(value = "import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @SneakyThrows
    public void importFromCsv(@RequestPart MultipartFile file) {
        InputStream inputStream = file.getInputStream();
        List<SkuGroup> cards = csvReader.importCsv(inputStream, SkuGroupDto.class).stream().map(skuGroupMapper::toEntity).toList();
        skuGroupService.save(cards);
    }


    @GetMapping(value = "export")
    @SneakyThrows
    public ResponseEntity<Resource> exportToCsv() {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        List<SkuGroupDto> customers = skuGroupService.findAll().stream().map(skuGroupMapper::toDto).toList();
        csvWriter.exportCsv(os, customers, SkuGroupDto.class);
        ByteArrayResource res = new ByteArrayResource(os.toByteArray());
        return ResponseEntity.ok(res);
    }
}
