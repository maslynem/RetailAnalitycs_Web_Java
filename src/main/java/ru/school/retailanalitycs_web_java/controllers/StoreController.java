package ru.school.retailanalitycs_web_java.controllers;

import jakarta.validation.Valid;
import lombok.SneakyThrows;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.school.retailanalitycs_web_java.dto.storeDto.StoreCreateDto;
import ru.school.retailanalitycs_web_java.dto.storeDto.StoreReadDto;
import ru.school.retailanalitycs_web_java.entities.tables.Store;
import ru.school.retailanalitycs_web_java.entities.tables.StoreId;
import ru.school.retailanalitycs_web_java.exceptions.notFoundExceptions.StoreNotFoundException;
import ru.school.retailanalitycs_web_java.mapper.StoreMapper;
import ru.school.retailanalitycs_web_java.services.StoreService;
import ru.school.retailanalitycs_web_java.utils.CsvReader;

import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("api/v1/stores")
public class StoreController {
    private final StoreService storeService;
    private final StoreMapper storeMapper;
    private final CsvReader<StoreCreateDto> csvReader;

    public StoreController(StoreService storeService, StoreMapper storeMapper, CsvReader<StoreCreateDto> csvReader) {
        this.storeService = storeService;
        this.storeMapper = storeMapper;
        this.csvReader = csvReader;
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
    public StoreReadDto findStoreById(@PathVariable Integer trStoreId,
                                      @PathVariable Integer skuId) {
        StoreId storeId = getId(trStoreId, skuId);
        return storeService.findById(storeId).map(storeMapper::toDto).orElseThrow(() -> new StoreNotFoundException(storeId));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public StoreReadDto create(@Valid @RequestBody StoreCreateDto storeDto) {
        Store store = storeMapper.toEntity(storeDto);
        Store save = storeService.save(store);
        return storeMapper.toDto(save);
    }

    @DeleteMapping("/{trStoreId}/{skuId}")
    public void delete(@PathVariable Integer trStoreId,
                       @PathVariable Integer skuId) {
        storeService.deleteById(getId(trStoreId, skuId));
    }

    @PostMapping(value = "import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @SneakyThrows
    public void importFromCsv(@RequestPart MultipartFile file) {
        InputStream inputStream = file.getInputStream();
        List<Store> stores = csvReader.importCsv(inputStream, StoreCreateDto.class).stream().map(storeMapper::toEntity).toList();
        storeService.save(stores);
    }

    private StoreId getId(Integer trStoreId, Integer skuId) {
        return new StoreId(trStoreId, skuId);
    }
}
