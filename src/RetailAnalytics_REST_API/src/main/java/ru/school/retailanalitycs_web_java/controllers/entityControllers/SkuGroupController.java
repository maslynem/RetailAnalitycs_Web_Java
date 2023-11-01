package ru.school.retailanalitycs_web_java.controllers.entityControllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.school.retailanalitycs_web_java.dto.entityDto.skuGroupDto.SkuGroupDto;
import ru.school.retailanalitycs_web_java.entities.tables.SkuGroup;
import ru.school.retailanalitycs_web_java.exceptions.notFoundExceptions.SkuGroupNotFoundException;
import ru.school.retailanalitycs_web_java.mapper.SkuGroupMapper;
import ru.school.retailanalitycs_web_java.services.entityServices.SkuGroupService;

import java.util.List;

@RestController
@RequestMapping("api/v1/sku-groups")
public class SkuGroupController {
    private final SkuGroupService skuGroupService;
    private final SkuGroupMapper skuGroupMapper;

    @Autowired
    public SkuGroupController(SkuGroupService skuGroupService, SkuGroupMapper skuGroupMapper) {
        this.skuGroupService = skuGroupService;
        this.skuGroupMapper = skuGroupMapper;
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

    @PostMapping(value = "all", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public SkuGroupDto save(@Valid @RequestBody SkuGroupDto skuGroupDto) {
        SkuGroup skuGroup = skuGroupMapper.toEntity(skuGroupDto);
        SkuGroup save = skuGroupService.save(skuGroup);
        skuGroupDto.setId(save.getId());
        return skuGroupDto;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void saveAll(@Valid @RequestBody List<SkuGroupDto> skuGroupsDto) {
        List<SkuGroup> skuGroups = skuGroupsDto.stream().map(skuGroupMapper::toEntity).toList();
        skuGroupService.save(skuGroups);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public SkuGroupDto update(@PathVariable Long id, @Valid @RequestBody SkuGroupDto skuGroupDto) {
        SkuGroup skuGroup = skuGroupMapper.toEntity(skuGroupDto);
        SkuGroup updated = skuGroupService.update(id, skuGroup);
        return skuGroupMapper.toDto(updated);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        skuGroupService.deleteById(id);
    }

}
