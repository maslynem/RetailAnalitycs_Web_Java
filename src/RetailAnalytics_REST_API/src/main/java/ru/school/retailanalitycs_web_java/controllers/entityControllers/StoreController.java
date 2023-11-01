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
import ru.school.retailanalitycs_web_java.dto.entityDto.storeDto.StoreCreateDto;
import ru.school.retailanalitycs_web_java.dto.entityDto.storeDto.StoreReadDto;
import ru.school.retailanalitycs_web_java.entities.tables.Sku;
import ru.school.retailanalitycs_web_java.entities.tables.Store;
import ru.school.retailanalitycs_web_java.entities.tables.StoreId;
import ru.school.retailanalitycs_web_java.exceptions.notFoundExceptions.StoreNotFoundException;
import ru.school.retailanalitycs_web_java.mapper.StoreMapper;
import ru.school.retailanalitycs_web_java.services.entityServices.StoreService;
import ru.school.retailanalitycs_web_java.utils.CsvReader;
import ru.school.retailanalitycs_web_java.utils.CsvWriter;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("api/v1/stores")
public class StoreController {
    private final StoreService storeService;
    private final StoreMapper storeMapper;
    private final CsvReader<StoreCreateDto> csvReader;
    private final CsvWriter<StoreCreateDto> csvWriter;

    public StoreController(StoreService storeService, StoreMapper storeMapper, CsvReader<StoreCreateDto> csvReader, CsvWriter<StoreCreateDto> csvWriter) {
        this.storeService = storeService;
        this.storeMapper = storeMapper;
        this.csvReader = csvReader;
        this.csvWriter = csvWriter;
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
    public StoreReadDto create(@Valid @RequestBody StoreCreateDto storeDto) {
        Store store = storeMapper.toEntity(storeDto);
        Store save = storeService.save(store);
        return storeMapper.toDto(save);
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

    @PostMapping(value = "import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @SneakyThrows
    public void importFromCsv(@RequestPart MultipartFile file) {
        InputStream inputStream = file.getInputStream();
        List<Store> stores = csvReader.importCsv(inputStream, StoreCreateDto.class).stream().map(storeMapper::toEntity).toList();
        storeService.save(stores);
    }

    @GetMapping(value = "export")
    @SneakyThrows
    public ResponseEntity<Resource> exportToCsv() {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        List<StoreCreateDto> customers = storeService.findAll().stream().map(storeMapper::toCreateDto).toList();
        csvWriter.exportCsv(os, customers, StoreCreateDto.class);
        ByteArrayResource res = new ByteArrayResource(os.toByteArray());
        return ResponseEntity.ok(res);
    }

    private StoreId getId(Long trStoreId, Long skuId) {
        return new StoreId(trStoreId, Sku.builder().id(skuId).build());
    }
}
