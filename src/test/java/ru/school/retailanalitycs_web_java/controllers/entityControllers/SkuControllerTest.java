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
import ru.school.retailanalitycs_web_java.dto.entityDto.skuDto.SkuCreateDto;
import ru.school.retailanalitycs_web_java.dto.entityDto.skuDto.SkuReadDto;
import ru.school.retailanalitycs_web_java.dto.entityDto.skuGroupDto.SkuGroupDto;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.school.retailanalitycs_web_java.exceptions.ExceptionCode.ENTITY_IS_NOT_VALID;
import static ru.school.retailanalitycs_web_java.exceptions.ExceptionCode.NOT_FOUND;

@SpringBootTest(classes = IntegrationsTestConfiguration.class)
@AutoConfigureMockMvc
@Transactional
class SkuControllerTest extends IntegrationTestBase {

    private static final Integer SKU_ID = 1;
    private static final Integer CUSTOMER_ID = 1;
    private static final Integer NOT_EXISTING_SKU_ID = 1000;
    private static final Integer NOT_EXISTING_SKU_GROUP_ID = 1000;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void findAllSkus() throws Exception {
        mockMvc.perform(get("/api/v1/skus"))
                .andExpect(status().isOk());
    }

    @Test
    void findSkusBy_page_1_size_3() throws Exception {
        SkuReadDto first = SkuReadDto.builder().id(4).skuName("ВНИИ Агропром Лимонад Байкал Red Line").skuGroup(getDtoWithId(6)).build();
        SkuReadDto second = SkuReadDto.builder().id(5).skuName("ООО Леторг Яблоки Антоновка Ревалон").skuGroup(getDtoWithId(5)).build();
        SkuReadDto third = SkuReadDto.builder().id(6).skuName("Kerton Яблоки Антоновка Поездка").skuGroup(getDtoWithId(2)).build();
        mockMvc.perform(get("/api/v1/skus?page=1&size=3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("content[0].id").value(first.getId()))
                .andExpect(jsonPath("content[0].skuName").value(first.getSkuName()))
                .andExpect(jsonPath("content[0].skuGroup.id").value(first.getSkuGroup().getId()))
                .andExpect(jsonPath("content[1].id").value(second.getId()))
                .andExpect(jsonPath("content[1].skuName").value(second.getSkuName()))
                .andExpect(jsonPath("content[1].skuGroup.id").value(second.getSkuGroup().getId()))
                .andExpect(jsonPath("content[2].id").value(third.getId()))
                .andExpect(jsonPath("content[2].skuName").value(third.getSkuName()))
                .andExpect(jsonPath("content[2].skuGroup.id").value(third.getSkuGroup().getId()))
                .andExpect(jsonPath("pageable.pageNumber").value(1))
                .andExpect(jsonPath("pageable.pageSize").value(3))
                .andExpect(jsonPath("$.totalPages").value(7))
                .andExpect(jsonPath("$.totalElements").value(20));
    }

    @Test
    void findSkuById() throws Exception {
        SkuReadDto skuDto = SkuReadDto.builder().id(SKU_ID).skuName("Kerton Бензин АИ-95 Поездка").skuGroup(getDtoWithId(4)).build();
        mockMvc.perform(get("/api/v1/skus/{SKU_ID}", SKU_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(skuDto.getId()))
                .andExpect(jsonPath("$.skuName").value(skuDto.getSkuName()))
                .andExpect(jsonPath("$.skuGroup.id").value(skuDto.getSkuGroup().getId()));
    }

    @Test
    void findNotExistingSku() throws Exception {
        mockMvc.perform(get("/api/v1/skus/{NOT_EXISTING_SKU_ID}", NOT_EXISTING_SKU_ID))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(NOT_FOUND.name()))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void create() throws Exception {
        SkuCreateDto skuDto = SkuCreateDto.builder().skuName("test").skuGroup(1).build();

        String requestJson = objectMapper.writeValueAsString(skuDto);
        mockMvc.perform(post("/api/v1/skus")
                        .contentType(APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(21))
                .andExpect(jsonPath("$.skuName").value("test"))
                .andExpect(jsonPath("$.skuGroup.id").value(CUSTOMER_ID));
    }

    @Test
    void createWithMissingSkuName_shouldReturnBadRequest() throws Exception {
        SkuCreateDto skuDto = SkuCreateDto.builder().skuGroup(1).build();
        String requestJson = objectMapper.writeValueAsString(skuDto);
        mockMvc.perform(post("/api/v1/skus")
                        .contentType(APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ENTITY_IS_NOT_VALID.name()))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void createWithMissingSkuGroup_shouldReturnBadRequest() throws Exception {
        SkuCreateDto skuDto = SkuCreateDto.builder().skuName("test").build();
        String requestJson = objectMapper.writeValueAsString(skuDto);
        mockMvc.perform(post("/api/v1/skus")
                        .contentType(APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ENTITY_IS_NOT_VALID.name()))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void createWithNotExistingSkuGroup_shouldReturnNotFound() throws Exception {
        SkuCreateDto skuDto = SkuCreateDto.builder().skuName("test").skuGroup(NOT_EXISTING_SKU_GROUP_ID).build();
        String requestJson = objectMapper.writeValueAsString(skuDto);
        mockMvc.perform(post("/api/v1/skus")
                        .contentType(APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(NOT_FOUND.name()))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void deleteSku() throws Exception {
        mockMvc.perform(delete("/api/v1/skus/{SKU_ID}", SKU_ID))
                .andExpect(status().isOk());
        mockMvc.perform(get("/api/v1/skus/{SKU_ID}", SKU_ID))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(NOT_FOUND.name()))
                .andExpect(jsonPath("$.message").exists());
    }

    private SkuGroupDto getDtoWithId(int id) {
        return SkuGroupDto.builder().id(id).build();
    }

}