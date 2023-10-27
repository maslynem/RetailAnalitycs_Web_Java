package ru.school.retailanalitycs_web_java.controllers.entityControllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import ru.school.retailanalitycs_web_java.IntegrationTestBase;
import ru.school.retailanalitycs_web_java.IntegrationsTestConfiguration;
import ru.school.retailanalitycs_web_java.dto.entityDto.skuDto.SkuReadDto;
import ru.school.retailanalitycs_web_java.dto.entityDto.storeDto.StoreCreateDto;
import ru.school.retailanalitycs_web_java.dto.entityDto.storeDto.StoreReadDto;
import ru.school.retailanalitycs_web_java.entities.tables.StoreId;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.school.retailanalitycs_web_java.exceptions.ExceptionCode.*;

@SpringBootTest(classes = IntegrationsTestConfiguration.class)
@AutoConfigureMockMvc
@Transactional
class StoreControllerTest extends IntegrationTestBase {

    private static final StoreId STORE_ID = new StoreId(1L, 2L);
    private static final Long TRANSACTION_STORE_ID = 1L;
    private static final Long SKU_ID = 1L;
    private static final Long NOT_EXISTING_SKU_ID = Long.MAX_VALUE;
    private static final StoreId NOT_EXISTING_STORE_ID = new StoreId(Long.MAX_VALUE, Long.MAX_VALUE);

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void findAllStores() throws Exception {
        mockMvc.perform(get("/api/v1/stores"))
                .andExpect(status().isOk());
    }

    @Test
    void findStoresBy_page_1_size_2() throws Exception {
        StoreReadDto first = StoreReadDto.builder()
                .transactionStoreId(1L)
                .sku(getSkuDtoWithId(3L))
                .skuPurchasePrice(41.3234384410658)
                .skuRetailPrice(55.9188140878772).build();
        StoreReadDto second = StoreReadDto.builder()
                .transactionStoreId(2L)
                .sku(getSkuDtoWithId(15L))
                .skuPurchasePrice(17.3098081747581)
                .skuRetailPrice(26.4077416772713).build();
        mockMvc.perform(get("/api/v1/stores?page=1&size=2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("content[0].transactionStoreId").value(first.getTransactionStoreId()))
                .andExpect(jsonPath("content[0].sku.id").value(first.getSku().getId()))
                .andExpect(jsonPath("content[0].skuPurchasePrice").value(first.getSkuPurchasePrice()))
                .andExpect(jsonPath("content[0].skuRetailPrice").value(first.getSkuRetailPrice()))
                .andExpect(jsonPath("content[1].transactionStoreId").value(second.getTransactionStoreId()))
                .andExpect(jsonPath("content[1].sku.id").value(second.getSku().getId()))
                .andExpect(jsonPath("content[1].skuPurchasePrice").value(second.getSkuPurchasePrice()))
                .andExpect(jsonPath("content[1].skuRetailPrice").value(second.getSkuRetailPrice()))
                .andExpect(jsonPath("pageable.pageNumber").value(1))
                .andExpect(jsonPath("pageable.pageSize").value(2))
                .andExpect(jsonPath("$.totalPages").value(6))
                .andExpect(jsonPath("$.totalElements").value(11));
    }

    @Test
    void findStoreById() throws Exception {
        StoreReadDto first = StoreReadDto.builder()
                .transactionStoreId(STORE_ID.getTransactionStoreId())
                .sku(getSkuDtoWithId(STORE_ID.getSkuId()))
                .skuPurchasePrice(78.0071743954938)
                .skuRetailPrice(104.882359988255).build();
        mockMvc.perform(get("/api/v1/stores/{trStoreId}/{skuId}", STORE_ID.getTransactionStoreId(), STORE_ID.getSkuId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transactionStoreId").value(first.getTransactionStoreId()))
                .andExpect(jsonPath("$.sku.id").value(first.getSku().getId()))
                .andExpect(jsonPath("$.skuPurchasePrice").value(first.getSkuPurchasePrice()))
                .andExpect(jsonPath("$.skuRetailPrice").value(first.getSkuRetailPrice()));
    }

    @Test
    void findNotExistingStore() throws Exception {
        mockMvc.perform(get("/api/v1/stores/{trStoreId}/{skuId}", NOT_EXISTING_STORE_ID.getTransactionStoreId(), NOT_EXISTING_STORE_ID.getSkuId()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(NOT_FOUND.name()))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void create() throws Exception {
        StoreCreateDto storeDto = StoreCreateDto.builder()
                .transactionStoreId(TRANSACTION_STORE_ID)
                .sku(SKU_ID)
                .skuPurchasePrice(0.1)
                .skuRetailPrice(0.1).build();

        String requestJson = objectMapper.writeValueAsString(storeDto);
        mockMvc.perform(post("/api/v1/stores")
                        .contentType(APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.transactionStoreId").value(TRANSACTION_STORE_ID))
                .andExpect(jsonPath("$.sku.id").value(SKU_ID))
                .andExpect(jsonPath("$.skuPurchasePrice").value(storeDto.getSkuPurchasePrice()))
                .andExpect(jsonPath("$.skuRetailPrice").value(storeDto.getSkuRetailPrice()));
    }

    @Test
    void createWithDuplicatedStoreId_shouldReturnBadRequest() throws Exception {
        StoreCreateDto storeDto = StoreCreateDto.builder()
                .transactionStoreId(STORE_ID.getTransactionStoreId())
                .sku(STORE_ID.getSkuId())
                .skuPurchasePrice(0.1)
                .skuRetailPrice(0.1).build();

        String requestJson = objectMapper.writeValueAsString(storeDto);
        mockMvc.perform(post("/api/v1/stores")
                        .contentType(APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(DUPLICATE_VALUE.name()))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void createWithMissingTransactionStoreId_shouldReturnBadRequest() throws Exception {
        StoreCreateDto storeDto = StoreCreateDto.builder()
                .sku(SKU_ID)
                .skuPurchasePrice(0.1)
                .skuRetailPrice(0.1).build();
        String requestJson = objectMapper.writeValueAsString(storeDto);
        mockMvc.perform(post("/api/v1/stores")
                        .contentType(APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ENTITY_IS_NOT_VALID.name()))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void createWithMissingSkuId_shouldReturnBadRequest() throws Exception {
        StoreCreateDto storeDto = StoreCreateDto.builder()
                .transactionStoreId(TRANSACTION_STORE_ID)
                .skuPurchasePrice(0.1)
                .skuRetailPrice(0.1).build();
        String requestJson = objectMapper.writeValueAsString(storeDto);
        mockMvc.perform(post("/api/v1/stores")
                        .contentType(APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ENTITY_IS_NOT_VALID.name()))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void createWithMissingSkuPurchasePrice_shouldReturnBadRequest() throws Exception {
        StoreCreateDto storeDto = StoreCreateDto.builder()
                .sku(SKU_ID)
                .transactionStoreId(TRANSACTION_STORE_ID)
                .skuRetailPrice(0.1).build();
        String requestJson = objectMapper.writeValueAsString(storeDto);
        mockMvc.perform(post("/api/v1/stores")
                        .contentType(APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ENTITY_IS_NOT_VALID.name()))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void createWithMissingSkuRetailPrice_shouldReturnBadRequest() throws Exception {
        StoreCreateDto storeDto = StoreCreateDto.builder()
                .sku(SKU_ID)
                .transactionStoreId(TRANSACTION_STORE_ID)
                .skuPurchasePrice(0.1).build();
        String requestJson = objectMapper.writeValueAsString(storeDto);
        mockMvc.perform(post("/api/v1/stores")
                        .contentType(APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ENTITY_IS_NOT_VALID.name()))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void createWithNotExistingSkuId_shouldReturnNotFound() throws Exception {
        StoreCreateDto storeDto = StoreCreateDto.builder()
                .transactionStoreId(TRANSACTION_STORE_ID)
                .sku(NOT_EXISTING_SKU_ID)
                .skuPurchasePrice(0.1)
                .skuRetailPrice(0.1).build();
        String requestJson = objectMapper.writeValueAsString(storeDto);
        mockMvc.perform(post("/api/v1/stores")
                        .contentType(APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(NOT_FOUND.name()))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void deleteStore() throws Exception {
        mockMvc.perform(delete("/api/v1/stores/{trStoreId}/{skuId}", STORE_ID.getTransactionStoreId(), STORE_ID.getSkuId()))
                .andExpect(status().isOk());
        mockMvc.perform(get("/api/v1/stores/{trStoreId}/{skuId}", STORE_ID.getTransactionStoreId(), STORE_ID.getSkuId()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(NOT_FOUND.name()))
                .andExpect(jsonPath("$.message").exists());
    }

    private SkuReadDto getSkuDtoWithId(Long id) {
        return SkuReadDto.builder().id(id).build();
    }

}