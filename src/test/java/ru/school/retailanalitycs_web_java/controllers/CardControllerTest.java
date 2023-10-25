package ru.school.retailanalitycs_web_java.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import ru.school.retailanalitycs_web_java.IntegrationTestBase;
import ru.school.retailanalitycs_web_java.IntegrationsTestConfiguration;
import ru.school.retailanalitycs_web_java.dto.CardDto;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = IntegrationsTestConfiguration.class)
@AutoConfigureMockMvc
@Transactional
class CardControllerTest extends IntegrationTestBase {

    private static final Integer CARD_ID = 1;
    private static final Integer CUSTOMER_ID = 1;
    private static final Integer NOT_EXISTING_CARD_ID = 1000;
    private static final Integer NOT_EXISTING_CUSTOMER_ID = 1000;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void findAllCards() throws Exception {
        mockMvc.perform(get("/api/v1/cards"))
                .andExpect(status().isOk());
    }

    @Test
    void findCardsBy_page_1_size_3() throws Exception {
        CardDto first = CardDto.builder().id(4).customer(5).build();
        CardDto second = CardDto.builder().id(5).customer(5).build();
        CardDto third = CardDto.builder().id(6).customer(6).build();
        mockMvc.perform(get("/api/v1/cards?page=1&size=3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("content[0].id").value(first.getId()))
                .andExpect(jsonPath("content[0].customer").value(first.getCustomer()))
                .andExpect(jsonPath("content[1].id").value(second.getId()))
                .andExpect(jsonPath("content[1].customer").value(second.getCustomer()))
                .andExpect(jsonPath("content[2].id").value(third.getId()))
                .andExpect(jsonPath("content[2].customer").value(third.getCustomer()))
                .andExpect(jsonPath("pageable.pageNumber").value(1))
                .andExpect(jsonPath("pageable.pageSize").value(3))
                .andExpect(jsonPath("$.totalPages").value(8))
                .andExpect(jsonPath("$.totalElements").value(22));
    }

    @Test
    void findCustomerById() throws Exception {
        CardDto cardDto = CardDto.builder().id(CARD_ID).customer(19).build();
        mockMvc.perform(get("/api/v1/cards/{CARD_ID}", CARD_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(cardDto.getId()))
                .andExpect(jsonPath("$.customer").value(cardDto.getCustomer()));
    }

    @Test
    void findNotExistingCustomer() throws Exception {
        mockMvc.perform(get("/api/v1/cards/{NOT_EXISTING_CARD_ID}", NOT_EXISTING_CARD_ID))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void create() throws Exception {
        CardDto cardDto = CardDto.builder().customer(CUSTOMER_ID).build();

        String requestJson = objectMapper.writeValueAsString(cardDto);
        mockMvc.perform(post("/api/v1/cards")
                        .contentType(APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(23))
                .andExpect(jsonPath("$.customer").value(cardDto.getCustomer()));
    }

    @Test
    void createWithMissingCustomer_shouldReturnBadRequest() throws Exception {
        CardDto cardDto = CardDto.builder().build();
        String requestJson = objectMapper.writeValueAsString(cardDto);
        mockMvc.perform(post("/api/v1/cards")
                        .contentType(APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("ENTITY_IS_NOT_VALID"))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void createWithNotExistingCustomer_shouldReturnNotFound() throws Exception {
        CardDto cardDto = CardDto.builder().customer(NOT_EXISTING_CUSTOMER_ID).build();
        String requestJson = objectMapper.writeValueAsString(cardDto);
        mockMvc.perform(post("/api/v1/cards")
                        .contentType(APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void deleteCard() throws Exception {
        mockMvc.perform(delete("/api/v1/cards/{CARD_ID}", CARD_ID))
                .andExpect(status().isOk());
        mockMvc.perform(get("/api/v1/cards/{CARD_ID}", CARD_ID))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message").exists());
    }

}