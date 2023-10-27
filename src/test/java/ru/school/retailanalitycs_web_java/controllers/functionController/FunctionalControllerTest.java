package ru.school.retailanalitycs_web_java.controllers.functionController;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import ru.school.retailanalitycs_web_java.IntegrationTestBase;
import ru.school.retailanalitycs_web_java.IntegrationsTestConfiguration;
import ru.school.retailanalitycs_web_java.controllers.functionController.requestEntity.FrequencyOfVisitRequest;
import ru.school.retailanalitycs_web_java.dto.entityDto.customerDto.CustomerDto;

import java.time.LocalDateTime;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.school.retailanalitycs_web_java.exceptions.ExceptionCode.ENTITY_IS_NOT_VALID;
import static ru.school.retailanalitycs_web_java.exceptions.ExceptionCode.NOT_FOUND;

@SpringBootTest(classes = IntegrationsTestConfiguration.class)
@AutoConfigureMockMvc
@Transactional
class FunctionalControllerTest extends IntegrationTestBase {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getFrequencyOfVisit_ShouldReturn4Entity() throws Exception {
        FrequencyOfVisitRequest frequencyOfVisitRequest = FrequencyOfVisitRequest.builder()
                .firstDate(LocalDateTime.parse("2022-08-18T00:00:00"))
                .lastDate(LocalDateTime.parse("2022-08-18T00:00:00"))
                .addedTransactions(1L)
                .maxChurnIndex(3.)
                .maxShareOfTransactions(70.)
                .allowableMarginShare(30.)
                .build();
        String requestJson = objectMapper.writeValueAsString(frequencyOfVisitRequest);
        mockMvc.perform(post("/api/v1/functions/frequency-of-visits")
                        .contentType(APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].customer.id").value(3))
                .andExpect(jsonPath("$[0].requiredTransactionsCount").value(1.0))
                .andExpect(jsonPath("$[0].groupName").value("Колбаса"))
                .andExpect(jsonPath("$[0].offerDiscountDepth").value(5.0))
                .andExpect(jsonPath("$[1].customer.id").value(5))
                .andExpect(jsonPath("$[1].requiredTransactionsCount").value(1.0))
                .andExpect(jsonPath("$[1].groupName").value("Колбаса"))
                .andExpect(jsonPath("$[1].offerDiscountDepth").value(5.0))
                .andExpect(jsonPath("$[2].customer.id").value(6))
                .andExpect(jsonPath("$[2].requiredTransactionsCount").value(1.0))
                .andExpect(jsonPath("$[2].groupName").value("Автомобили"))
                .andExpect(jsonPath("$[2].offerDiscountDepth").value(10.0))
                .andExpect(jsonPath("$[3].customer.id").value(19))
                .andExpect(jsonPath("$[3].requiredTransactionsCount").value(1.0))
                .andExpect(jsonPath("$[3].groupName").value("Чипсы"))
                .andExpect(jsonPath("$[3].offerDiscountDepth").value(10.0));
    }

}