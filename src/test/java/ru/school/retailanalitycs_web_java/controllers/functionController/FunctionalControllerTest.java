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
import ru.school.retailanalitycs_web_java.controllers.functionController.requestEntity.MarginGrowthOfferRequest;
import ru.school.retailanalitycs_web_java.controllers.functionController.requestEntity.PersonalOfferByDatesRequest;
import ru.school.retailanalitycs_web_java.controllers.functionController.requestEntity.PersonalOfferByTransactionsNumberRequest;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

    @Test
    void getPersonalOfferByDates_ShouldReturn5Entities() throws Exception {
        PersonalOfferByDatesRequest request = PersonalOfferByDatesRequest.builder()
                .firstDate(LocalDate.of(2016, 1, 1))
                .lastDate(LocalDate.of(2023, 1, 1))
                .coefficientOfAverageCheckIncrease(1.0)
                .maxChurnIndex(1.4)
                .maxShareOfTransactionsWithDiscount(80)
                .allowableShareOfMargin(50)
                .build();
        String requestJson = objectMapper.writeValueAsString(request);
        mockMvc.perform(post("/api/v1/functions/personal-offer-growth-by-dates")
                        .contentType(APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].customer.id").value(3))
                .andExpect(jsonPath("$[0].requiredCheckMeasure").value(582.2256957375))
                .andExpect(jsonPath("$[0].groupName").value("Колбаса"))
                .andExpect(jsonPath("$[0].offerDiscountDepth").value(5.0))
                .andExpect(jsonPath("$[1].customer.id").value(5))
                .andExpect(jsonPath("$[1].requiredCheckMeasure").value(631.5295555411429))
                .andExpect(jsonPath("$[1].groupName").value("Зарядки"))
                .andExpect(jsonPath("$[1].offerDiscountDepth").value(10.0))
                .andExpect(jsonPath("$[2].customer.id").value(6))
                .andExpect(jsonPath("$[2].requiredCheckMeasure").value(1054.754847696829))
                .andExpect(jsonPath("$[2].groupName").value("Чипсы"))
                .andExpect(jsonPath("$[2].offerDiscountDepth").value(15.0))
                .andExpect(jsonPath("$[3].customer.id").value(11))
                .andExpect(jsonPath("$[3].requiredCheckMeasure").value(734.8960367220001))
                .andExpect(jsonPath("$[3].groupName").value("Чипсы"))
                .andExpect(jsonPath("$[3].offerDiscountDepth").value(5.0))
                .andExpect(jsonPath("$[4].customer.id").value(19))
                .andExpect(jsonPath("$[4].requiredCheckMeasure").value(928.8021049543752))
                .andExpect(jsonPath("$[4].groupName").value("Чипсы"))
                .andExpect(jsonPath("$[4].offerDiscountDepth").value(10.0));
    }

    @Test
    void getPersonalOfferByTransactionsNumber_ShouldReturn4Entities() throws Exception {
        PersonalOfferByTransactionsNumberRequest request = PersonalOfferByTransactionsNumberRequest.builder()
                .numberOfTransactions(100)
                .coefficientOfAverageCheckIncrease(1.15)
                .maxChurnIndex(3.)
                .maxShareOfTransactionsWithDiscount(70)
                .allowableShareOfMargin(30)
                .build();
        String requestJson = objectMapper.writeValueAsString(request);
        mockMvc.perform(post("/api/v1/functions/personal-offer-growth-by-transactions-number")
                        .contentType(APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].customer.id").value(3))
                .andExpect(jsonPath("$[0].requiredCheckMeasure").value(669.559550098125))
                .andExpect(jsonPath("$[0].groupName").value("Колбаса"))
                .andExpect(jsonPath("$[0].offerDiscountDepth").value(5.0))
                .andExpect(jsonPath("$[1].customer.id").value(5))
                .andExpect(jsonPath("$[1].requiredCheckMeasure").value(726.2589888723145))
                .andExpect(jsonPath("$[1].groupName").value("Колбаса"))
                .andExpect(jsonPath("$[1].offerDiscountDepth").value(5.0))
                .andExpect(jsonPath("$[2].customer.id").value(6))
                .andExpect(jsonPath("$[2].requiredCheckMeasure").value(1212.9680748513535))
                .andExpect(jsonPath("$[2].groupName").value("Автомобили"))
                .andExpect(jsonPath("$[2].offerDiscountDepth").value(10.0))
                .andExpect(jsonPath("$[3].customer.id").value(19))
                .andExpect(jsonPath("$[3].requiredCheckMeasure").value(1068.1224206975314))
                .andExpect(jsonPath("$[3].groupName").value("Чипсы"))
                .andExpect(jsonPath("$[3].offerDiscountDepth").value(10.0));
    }

    @Test
    void getMarginGrowthOffer_ShouldReturn1Entity() throws Exception {
        MarginGrowthOfferRequest request = MarginGrowthOfferRequest.builder()
                .groupsNumber(5L)
                .maxChurnIndex(3.)
                .maxStabilityIndex(0.5)
                .maxSkuShare(100.)
                .allowableMarginShare(30.)
                .build();
        String requestJson = objectMapper.writeValueAsString(request);
        mockMvc.perform(post("/api/v1/functions/margin-growth-offer")
                        .contentType(APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].customer.id").value(11))
                .andExpect(jsonPath("$[0].skuName").value("Heipz GmbH Ручка шариковая Ромашка"))
                .andExpect(jsonPath("$[0].offerDiscountDepth").value(5.0));
    }

}