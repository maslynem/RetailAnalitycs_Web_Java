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
import ru.school.retailanalitycs_web_java.dto.entityDto.skuGroupDto.SkuGroupDto;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.school.retailanalitycs_web_java.exceptions.ExceptionCode.*;

@SpringBootTest(classes = IntegrationsTestConfiguration.class)
@AutoConfigureMockMvc
@Transactional
class SkuGroupControllerTest extends IntegrationTestBase {
    private static final Long SKU_GROUP_ID = 1L;
    private static final String EXISTING_GROUP_NAME = "Чипсы";
    private static final Long NOT_EXISTING_SKU_GROUP_ID = 1000L;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void findAllSkuGroups() throws Exception {
        mockMvc.perform(get("/api/v1/sku-groups"))
                .andExpect(status().isOk());
    }

    @Test
    void findSkuGroupsBy_page_1_size_3() throws Exception {
        SkuGroupDto first = SkuGroupDto.builder().id(4L).groupName("Автомобили").build();
        SkuGroupDto second = SkuGroupDto.builder().id(5L).groupName("Зарядки").build();
        SkuGroupDto third = SkuGroupDto.builder().id(6L).groupName("Сухари").build();
        mockMvc.perform(get("/api/v1/sku-groups?page=1&size=3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("content[0].id").value(first.getId()))
                .andExpect(jsonPath("content[0].groupName").value(first.getGroupName()))
                .andExpect(jsonPath("content[1].id").value(second.getId()))
                .andExpect(jsonPath("content[1].groupName").value(second.getGroupName()))
                .andExpect(jsonPath("content[2].id").value(third.getId()))
                .andExpect(jsonPath("content[2].groupName").value(third.getGroupName()))
                .andExpect(jsonPath("pageable.pageNumber").value(1))
                .andExpect(jsonPath("pageable.pageSize").value(3))
                .andExpect(jsonPath("$.totalPages").value(7))
                .andExpect(jsonPath("$.totalElements").value(20));
    }

    @Test
    void findSkuGroupById() throws Exception {
        SkuGroupDto first = SkuGroupDto.builder().id(SKU_GROUP_ID).groupName("Чипсы").build();
        mockMvc.perform(get("/api/v1/sku-groups/{SkuGroup_ID}", SKU_GROUP_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(SKU_GROUP_ID))
                .andExpect(jsonPath("$.groupName").value(first.getGroupName()));
    }

    @Test
    void findNotExistingSkuGroup() throws Exception {
        mockMvc.perform(get("/api/v1/sku-groups/{NOT_EXISTING_ID}", NOT_EXISTING_SKU_GROUP_ID))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(NOT_FOUND.name()))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void create() throws Exception {
        SkuGroupDto skuGroupDto = SkuGroupDto.builder().groupName("test").build();

        String requestJson = objectMapper.writeValueAsString(skuGroupDto);
        mockMvc.perform(post("/api/v1/sku-groups")
                        .contentType(APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(21))
                .andExpect(jsonPath("$.groupName").value(skuGroupDto.getGroupName()));
    }

    @Test
    void createWithMissingGroupName_shouldReturnBadRequest() throws Exception {
        SkuGroupDto skuGroupDto = SkuGroupDto.builder().build();
        String requestJson = objectMapper.writeValueAsString(skuGroupDto);
        mockMvc.perform(post("/api/v1/sku-groups")
                        .contentType(APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ENTITY_IS_NOT_VALID.name()))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void createWithExistingGroupName_shouldReturnBadRequest() throws Exception {
        SkuGroupDto skuGroupDto = SkuGroupDto.builder().groupName(EXISTING_GROUP_NAME).build();
        String requestJson = objectMapper.writeValueAsString(skuGroupDto);
        mockMvc.perform(post("/api/v1/sku-groups")
                        .contentType(APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(DUPLICATE_VALUE.name()))
                .andExpect(jsonPath("$.message").exists());
    }

// todo
//    @Test
//    void update() throws Exception {
//        SkuGroupCreateEditDto SkuGroupDto = SkuGroupCreateEditDto.builder()
//                .name("test")
//                .email("test@mail.ru")
//                .companyName("IvanCorp")
//                .age(19)
//                .build();
//        String requestJson = objectMapper.writeValueAsString(SkuGroupDto);
//        mockMvc.perform(put("/api/v1/sku-groups/{SkuGroup_ID}", SkuGroup_ID)
//                        .contentType(APPLICATION_JSON)
//                        .content(requestJson))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(SkuGroup_ID))
//                .andExpect(jsonPath("$.name").value("test"))
//                .andExpect(jsonPath("$.email").value("test@mail.ru"))
//                .andExpect(jsonPath("$.age").value(19));
//    }
// todo
//    @Test
//    void updateNotExistingSkuGroup() throws Exception {
//        SkuGroupCreateEditDto SkuGroupDto = SkuGroupCreateEditDto.builder()
//                .name("test")
//                .email("test@mail.ru")
//                .age(19)
//                .build();
//        String requestJson = objectMapper.writeValueAsString(SkuGroupDto);
//        mockMvc.perform(put("/api/v1/sku-groups/{NOT_EXISTING_ID}", NOT_EXISTING_ID)
//                        .contentType(APPLICATION_JSON)
//                        .content(requestJson))
//                .andExpect(status().isNotFound())
//                .andExpect(jsonPath("$.message").exists());
//    }

    @Test
    void deleteSkuGroup() throws Exception {
        mockMvc.perform(delete("/api/v1/sku-groups/{SkuGroup_ID}", SKU_GROUP_ID))
                .andExpect(status().isOk());
        mockMvc.perform(get("/api/v1/sku-groups/{SkuGroup_ID}", SKU_GROUP_ID))
                .andExpect(status().isNotFound());
    }
}