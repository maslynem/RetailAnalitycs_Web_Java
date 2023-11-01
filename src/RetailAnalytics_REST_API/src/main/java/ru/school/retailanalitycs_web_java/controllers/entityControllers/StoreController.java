package ru.school.retailanalitycs_web_java.controllers.entityControllers;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.school.retailanalitycs_web_java.dto.entityDto.storeDto.StoreCreateDto;
import ru.school.retailanalitycs_web_java.dto.entityDto.storeDto.StoreReadDto;
import ru.school.retailanalitycs_web_java.entities.tables.Sku;
import ru.school.retailanalitycs_web_java.entities.tables.Store;
import ru.school.retailanalitycs_web_java.entities.tables.StoreId;
import ru.school.retailanalitycs_web_java.exceptions.notFoundExceptions.StoreNotFoundException;
import ru.school.retailanalitycs_web_java.mapper.StoreMapper;
import ru.school.retailanalitycs_web_java.services.entityServices.StoreService;

import java.util.List;

@RestController
@RequestMapping("api/v1/stores")
public class StoreController {
    private final StoreService storeService;
    private final StoreMapper storeMapper;

    public StoreController(StoreService storeService, StoreMapper storeMapper) {
        this.storeService = storeService;
        this.storeMapper = storeMapper;
    }

    @GetMapping
    public List<StoreReadDto> findAllStores() {
        return storeService.findAll().stream().map(storeMapper::toDto).toList();
    }

    @GetMapping(params = {"page", "size"})
    public Page<StoreReadDto> findAllStoresByPage(@RequestParam("page") int page,
                                                  @RequestParam("size") int size) {
        return storeService.findAllByPage(page, size).map(storeMapper::toDto);
    }

    @GetMapping("/{trStoreId}/{skuId}")
    public StoreReadDto findStoreById(@PathVariable Long trStoreId,
                                      @PathVariable Long skuId) {
        StoreId storeId = getId(trStoreId, skuId);
        return storeService.findById(storeId).map(storeMapper::toDto).orElseThrow(() -> new StoreNotFoundException(storeId));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public StoreReadDto save(@Valid @RequestBody StoreCreateDto storeDto) {
        Store store = storeMapper.toEntity(storeDto);
        Store save = storeService.save(store);
        return storeMapper.toDto(save);
    }

    @PostMapping(value = "all", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void saveAll(@Valid @RequestBody List<StoreCreateDto> storesDto) {
        List<Store> stores = storesDto.stream().map(storeMapper::toEntity).toList();
        storeService.save(stores);
    }

    @PutMapping(value = "/{trStoreId}/{skuId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public StoreReadDto update(@PathVariable Long trStoreId,
                               @PathVariable Long skuId,
                               @Valid @RequestBody StoreCreateDto dto) {
        StoreId checkId = getId(trStoreId, skuId);
        Store entity = storeMapper.toEntity(dto);
        Store updated = storeService.update(checkId, entity);
        return storeMapper.toDto(updated);
    }

    @DeleteMapping("/{trStoreId}/{skuId}")
    public void delete(@PathVariable Long trStoreId,
                       @PathVariable Long skuId) {
        storeService.deleteById(getId(trStoreId, skuId));
    }


    private StoreId getId(Long trStoreId, Long skuId) {
        return new StoreId(trStoreId, Sku.builder().id(skuId).build());
    }
}
