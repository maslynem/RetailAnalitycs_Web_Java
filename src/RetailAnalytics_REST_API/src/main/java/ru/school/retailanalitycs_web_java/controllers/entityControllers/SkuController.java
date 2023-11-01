package ru.school.retailanalitycs_web_java.controllers.entityControllers;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.school.retailanalitycs_web_java.dto.entityDto.skuDto.SkuCreateDto;
import ru.school.retailanalitycs_web_java.dto.entityDto.skuDto.SkuReadDto;
import ru.school.retailanalitycs_web_java.entities.tables.Sku;
import ru.school.retailanalitycs_web_java.exceptions.notFoundExceptions.SkuNotFoundException;
import ru.school.retailanalitycs_web_java.mapper.SkuMapper;
import ru.school.retailanalitycs_web_java.services.entityServices.SkuService;

import java.util.List;

@RestController
@RequestMapping("api/v1/skus")
public class SkuController {
    private final SkuService skuService;
    private final SkuMapper skuMapper;

    public SkuController(SkuService skuService, SkuMapper skuMapper) {
        this.skuService = skuService;
        this.skuMapper = skuMapper;
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
    public SkuReadDto save(@Valid @RequestBody SkuCreateDto skuDto) {
        Sku sku = skuMapper.toEntity(skuDto);
        Sku save = skuService.save(sku);
        return skuMapper.toDto(save);
    }

    @PostMapping(value = "all", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void saveAll(@Valid @RequestBody List<SkuCreateDto> skusDto) {
        List<Sku> skus = skusDto.stream().map(skuMapper::toEntity).toList();
        skuService.save(skus);
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

}
