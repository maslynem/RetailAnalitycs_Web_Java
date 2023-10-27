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
import ru.school.retailanalitycs_web_java.dto.entityDto.checkDto.CheckCreateDto;
import ru.school.retailanalitycs_web_java.dto.entityDto.checkDto.CheckReadDto;
import ru.school.retailanalitycs_web_java.dto.entityDto.skuDto.SkuReadDto;
import ru.school.retailanalitycs_web_java.dto.entityDto.transactionDto.TransactionReadDto;
import ru.school.retailanalitycs_web_java.entities.tables.CheckId;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.school.retailanalitycs_web_java.exceptions.ExceptionCode.*;

@SpringBootTest(classes = IntegrationsTestConfiguration.class)
@AutoConfigureMockMvc
@Transactional
class CheckControllerTest extends IntegrationTestBase {

    private static final CheckId CHECK_ID = new CheckId(1, 2);
    private static final Integer TRANSACTION_ID = 1;
    private static final Integer SKU_ID = 1;
    private static final Integer NOT_EXISTING_SKU_ID = Integer.MAX_VALUE;
    private static final Integer NOT_EXISTING_TRANSACTION_ID = Integer.MAX_VALUE;
    private static final CheckId NOT_EXISTING_CHECK_ID = new CheckId(Integer.MAX_VALUE, Integer.MAX_VALUE);

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void findAllChecks() throws Exception {
        mockMvc.perform(get("/api/v1/checks"))
                .andExpect(status().isOk());
    }

    @Test
    void findChecksBy_page_1_size_2() throws Exception {
        CheckReadDto first = CheckReadDto.builder()
                .transaction(getTransactionDtoWithId(3))
                .sku(getSkuDtoWithId(15))
                .skuAmount(4.155730297)
                .skuSum(294.7881466)
                .skuSumPaid(294.7881466)
                .skuDiscount(0.0)
                .build();
        CheckReadDto second = CheckReadDto.builder()
                .transaction(getTransactionDtoWithId(4))
                .sku(getSkuDtoWithId(3))
                .skuAmount(2.15546012)
                .skuSum(79.97275934)
                .skuSumPaid(63.14871001)
                .skuDiscount(16.82404933)
                .build();

        mockMvc.perform(get("/api/v1/checks?page=1&size=2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("content[0].transaction.id").value(first.getTransaction().getId()))
                .andExpect(jsonPath("content[0].sku.id").value(first.getSku().getId()))
                .andExpect(jsonPath("content[0].skuAmount").value(first.getSkuAmount()))
                .andExpect(jsonPath("content[0].skuSum").value(first.getSkuSum()))
                .andExpect(jsonPath("content[0].skuSumPaid").value(first.getSkuSumPaid()))
                .andExpect(jsonPath("content[0].skuDiscount").value(first.getSkuDiscount()))
                .andExpect(jsonPath("content[1].transaction.id").value(second.getTransaction().getId()))
                .andExpect(jsonPath("content[1].sku.id").value(second.getSku().getId()))
                .andExpect(jsonPath("content[1].skuAmount").value(second.getSkuAmount()))
                .andExpect(jsonPath("content[1].skuSum").value(second.getSkuSum()))
                .andExpect(jsonPath("content[1].skuSumPaid").value(second.getSkuSumPaid()))
                .andExpect(jsonPath("content[1].skuDiscount").value(second.getSkuDiscount()))
                .andExpect(jsonPath("pageable.pageNumber").value(1))
                .andExpect(jsonPath("pageable.pageSize").value(2))
                .andExpect(jsonPath("$.totalPages").value(100))
                .andExpect(jsonPath("$.totalElements").value(200));
    }

    @Test
    void findCheckById() throws Exception {
        CheckReadDto first = CheckReadDto.builder()
                .transaction(getTransactionDtoWithId(CHECK_ID.getTransactionId()))
                .sku(getSkuDtoWithId(CHECK_ID.getSkuId()))
                .skuAmount(3.20664673)
                .skuSum(336.3206767)
                .skuSumPaid(190.2882909)
                .skuDiscount(146.0323858)
                .build();
        mockMvc.perform(get("/api/v1/checks/{trId}/{skuId}", CHECK_ID.getTransactionId(), CHECK_ID.getSkuId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transaction.id").value(first.getTransaction().getId()))
                .andExpect(jsonPath("$.sku.id").value(first.getSku().getId()))
                .andExpect(jsonPath("$.skuAmount").value(first.getSkuAmount()))
                .andExpect(jsonPath("$.skuSum").value(first.getSkuSum()))
                .andExpect(jsonPath("$.skuSumPaid").value(first.getSkuSumPaid()))
                .andExpect(jsonPath("$.skuDiscount").value(first.getSkuDiscount()));
    }

    @Test
    void findNotExistingCheck() throws Exception {
        mockMvc.perform(get("/api/v1/checks/{trCheckId}/{skuId}", NOT_EXISTING_CHECK_ID.getTransactionId(), NOT_EXISTING_CHECK_ID.getSkuId()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(NOT_FOUND.name()))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void create() throws Exception {
        CheckCreateDto checkDto = CheckCreateDto.builder()
                .transactionId(TRANSACTION_ID)
                .skuId(SKU_ID)
                .skuAmount(3.20664673)
                .skuSum(336.3206767)
                .skuSumPaid(190.2882909)
                .skuDiscount(146.0323858)
                .build();

        String requestJson = objectMapper.writeValueAsString(checkDto);
        mockMvc.perform(post("/api/v1/checks")
                        .contentType(APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.transaction.id").value(TRANSACTION_ID))
                .andExpect(jsonPath("$.sku.id").value(SKU_ID))
                .andExpect(jsonPath("$.skuAmount").value(checkDto.getSkuAmount()))
                .andExpect(jsonPath("$.skuSum").value(checkDto.getSkuSum()))
                .andExpect(jsonPath("$.skuSumPaid").value(checkDto.getSkuSumPaid()))
                .andExpect(jsonPath("$.skuDiscount").value(checkDto.getSkuDiscount()));
    }
    @Test
    void createWithDuplicatedCheckId_shouldReturnBadRequest() throws Exception {
        CheckCreateDto checkDto = CheckCreateDto.builder()
                .transactionId(CHECK_ID.getTransactionId())
                .skuId(CHECK_ID.getSkuId())
                .skuAmount(3.20664673)
                .skuSum(336.3206767)
                .skuSumPaid(190.2882909)
                .skuDiscount(146.0323858)
                .build();

        String requestJson = objectMapper.writeValueAsString(checkDto);
        mockMvc.perform(post("/api/v1/checks")
                        .contentType(APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(DUPLICATE_VALUE.name()))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void createWithMissingTransactionId_shouldReturnBadRequest() throws Exception {
        CheckCreateDto checkDto = CheckCreateDto.builder()
                .skuId(SKU_ID)
                .skuAmount(3.20664673)
                .skuSum(336.3206767)
                .skuSumPaid(190.2882909)
                .skuDiscount(146.0323858)
                .build();
        String requestJson = objectMapper.writeValueAsString(checkDto);
        mockMvc.perform(post("/api/v1/checks")
                        .contentType(APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ENTITY_IS_NOT_VALID.name()))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void createWithMissingSkuId_shouldReturnBadRequest() throws Exception {
        CheckCreateDto checkDto = CheckCreateDto.builder()
                .transactionId(TRANSACTION_ID)
                .skuAmount(3.20664673)
                .skuSum(336.3206767)
                .skuSumPaid(190.2882909)
                .skuDiscount(146.0323858)
                .build();
        String requestJson = objectMapper.writeValueAsString(checkDto);
        mockMvc.perform(post("/api/v1/checks")
                        .contentType(APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ENTITY_IS_NOT_VALID.name()))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void createWithMissingSkuAmount_shouldReturnBadRequest() throws Exception {
        CheckCreateDto checkDto = CheckCreateDto.builder()
                .transactionId(TRANSACTION_ID)
                .skuId(SKU_ID)
                .skuSum(336.3206767)
                .skuSumPaid(190.2882909)
                .skuDiscount(146.0323858)
                .build();
        String requestJson = objectMapper.writeValueAsString(checkDto);
        mockMvc.perform(post("/api/v1/checks")
                        .contentType(APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ENTITY_IS_NOT_VALID.name()))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void createWithMissingSkuSum_shouldReturnBadRequest() throws Exception {
        CheckCreateDto checkDto = CheckCreateDto.builder()
                .transactionId(TRANSACTION_ID)
                .skuId(SKU_ID)
                .skuAmount(3.20664673)
                .skuSumPaid(190.2882909)
                .skuDiscount(146.0323858)
                .build();
        String requestJson = objectMapper.writeValueAsString(checkDto);
        mockMvc.perform(post("/api/v1/checks")
                        .contentType(APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ENTITY_IS_NOT_VALID.name()))
                .andExpect(jsonPath("$.message").exists());
    }
    @Test
    void createWithMissingSkuSumPaid_shouldReturnBadRequest() throws Exception {
        CheckCreateDto checkDto = CheckCreateDto.builder()
                .transactionId(TRANSACTION_ID)
                .skuId(SKU_ID)
                .skuAmount(3.20664673)
                .skuSum(336.3206767)
                .skuDiscount(146.0323858)
                .build();
        String requestJson = objectMapper.writeValueAsString(checkDto);
        mockMvc.perform(post("/api/v1/checks")
                        .contentType(APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ENTITY_IS_NOT_VALID.name()))
                .andExpect(jsonPath("$.message").exists());
    }
    @Test
    void createWithMissingSkuDiscount_shouldReturnBadRequest() throws Exception {
        CheckCreateDto checkDto = CheckCreateDto.builder()
                .transactionId(TRANSACTION_ID)
                .skuId(SKU_ID)
                .skuAmount(3.20664673)
                .skuSum(336.3206767)
                .skuSumPaid(190.2882909)
                .build();
        String requestJson = objectMapper.writeValueAsString(checkDto);
        mockMvc.perform(post("/api/v1/checks")
                        .contentType(APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ENTITY_IS_NOT_VALID.name()))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void createWithNotExistingTransactionId_shouldReturnNotFound() throws Exception {
        CheckCreateDto checkDto = CheckCreateDto.builder()
                .transactionId(NOT_EXISTING_TRANSACTION_ID)
                .skuId(SKU_ID)
                .skuAmount(3.20664673)
                .skuSum(336.3206767)
                .skuSumPaid(190.2882909)
                .skuDiscount(146.0323858)
                .build();
        String requestJson = objectMapper.writeValueAsString(checkDto);
        mockMvc.perform(post("/api/v1/checks")
                        .contentType(APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(NOT_FOUND.name()))
                .andExpect(jsonPath("$.message").exists());
    }
    @Test
    void createWithNotExistingSkuId_shouldReturnNotFound() throws Exception {
        CheckCreateDto checkDto = CheckCreateDto.builder()
                .transactionId(TRANSACTION_ID)
                .skuId(NOT_EXISTING_SKU_ID)
                .skuAmount(3.20664673)
                .skuSum(336.3206767)
                .skuSumPaid(190.2882909)
                .skuDiscount(146.0323858)
                .build();
        String requestJson = objectMapper.writeValueAsString(checkDto);
        mockMvc.perform(post("/api/v1/checks")
                        .contentType(APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(NOT_FOUND.name()))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void deleteCheck() throws Exception {
        mockMvc.perform(delete("/api/v1/checks/{trCheckId}/{skuId}", CHECK_ID.getTransactionId(), CHECK_ID.getSkuId()))
                .andExpect(status().isOk());
        mockMvc.perform(get("/api/v1/checks/{trCheckId}/{skuId}", CHECK_ID.getTransactionId(), CHECK_ID.getSkuId()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(NOT_FOUND.name()))
                .andExpect(jsonPath("$.message").exists());
    }

    private SkuReadDto getSkuDtoWithId(int id) {
        return SkuReadDto.builder().id(id).build();
    }

    private TransactionReadDto getTransactionDtoWithId(int id) {
        return TransactionReadDto.builder().id(id).build();
    }

}