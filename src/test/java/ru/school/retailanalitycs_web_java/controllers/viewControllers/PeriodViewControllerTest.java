package ru.school.retailanalitycs_web_java.controllers.viewControllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import ru.school.retailanalitycs_web_java.IntegrationTestBase;
import ru.school.retailanalitycs_web_java.IntegrationsTestConfiguration;
import ru.school.retailanalitycs_web_java.dto.entityDto.customerDto.CustomerDto;
import ru.school.retailanalitycs_web_java.dto.entityDto.skuGroupDto.SkuGroupDto;
import ru.school.retailanalitycs_web_java.dto.viewDto.PeriodViewDto;
import ru.school.retailanalitycs_web_java.entities.views.periodView.PeriodViewId;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.school.retailanalitycs_web_java.exceptions.ExceptionCode.NOT_FOUND;

@SpringBootTest(classes = IntegrationsTestConfiguration.class)
@AutoConfigureMockMvc
class PeriodViewControllerTest extends IntegrationTestBase {
    private static final PeriodViewId PERIOD_VIEW_ID = new PeriodViewId(4L, 7L);
    private static final PeriodViewId NOT_EXISTING_PERIOD_VIEW_ID = new PeriodViewId(Long.MAX_VALUE, Long.MAX_VALUE);

    @Autowired
    private MockMvc mockMvc;

    @Test
    void findAllPeriods() throws Exception {
        mockMvc.perform(get("/api/v1/views/periods"))
                .andExpect(status().isOk());
    }

    @Test
    void findPeriodsBy_page_1_size_2() throws Exception {
        PeriodViewDto first = PeriodViewDto.builder()
                .customer(getCustomerDtoWithId(1L))
                .skuGroup(getSkuGroupDtoWithId(3L))
                .groupPurchase(5L)
                .groupFrequency(171.54100694444443)
                .groupMinDiscount(0.30488100929709167)
                .build();

        PeriodViewDto second = PeriodViewDto.builder()
                .customer(getCustomerDtoWithId(1L))
                .skuGroup(getSkuGroupDtoWithId(5L))
                .groupPurchase(2L)
                .groupFrequency(371.9934490740741)
                .groupMinDiscount(0.12205489263006249)
                .build();

        mockMvc.perform(get("/api/v1/views/periods?page=1&size=2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("content[0].customer.id").value(first.getCustomer().getId()))
                .andExpect(jsonPath("content[0].group.id").value(first.getSkuGroup().getId()))
                .andExpect(jsonPath("content[0].firstGroupPurchaseDate").exists())
                .andExpect(jsonPath("content[0].lastGroupPurchaseDate").exists())
                .andExpect(jsonPath("content[0].groupPurchase").value(first.getGroupPurchase()))
                .andExpect(jsonPath("content[0].groupFrequency").value(first.getGroupFrequency()))
                .andExpect(jsonPath("content[0].groupMinDiscount").value(first.getGroupMinDiscount()))
                .andExpect(jsonPath("content[1].customer.id").value(second.getCustomer().getId()))
                .andExpect(jsonPath("content[1].group.id").value(second.getSkuGroup().getId()))
                .andExpect(jsonPath("content[1].firstGroupPurchaseDate").exists())
                .andExpect(jsonPath("content[1].lastGroupPurchaseDate").exists())
                .andExpect(jsonPath("content[1].groupPurchase").value(second.getGroupPurchase()))
                .andExpect(jsonPath("content[1].groupFrequency").value(second.getGroupFrequency()))
                .andExpect(jsonPath("content[1].groupMinDiscount").value(second.getGroupMinDiscount()))

                .andExpect(jsonPath("pageable.pageNumber").value(1))
                .andExpect(jsonPath("pageable.pageSize").value(2))
                .andExpect(jsonPath("$.totalPages").value(28))
                .andExpect(jsonPath("$.totalElements").value(55));
    }

    @Test
    void findPeriodById() throws Exception {
        PeriodViewDto first = PeriodViewDto.builder()
                .customer(getCustomerDtoWithId(PERIOD_VIEW_ID.getCustomerId()))
                .skuGroup(getSkuGroupDtoWithId(PERIOD_VIEW_ID.getSkuGroupId()))
                .groupPurchase(1L)
                .groupFrequency(1.0)
                .groupMinDiscount(0.4335659145964605)
                .build();

        mockMvc.perform(get("/api/v1/views/periods/{customer_id}/{group_id}", PERIOD_VIEW_ID.getCustomerId(), PERIOD_VIEW_ID.getSkuGroupId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customer.id").value(first.getCustomer().getId()))
                .andExpect(jsonPath("$.group.id").value(first.getSkuGroup().getId()))
                .andExpect(jsonPath("$.firstGroupPurchaseDate").exists())
                .andExpect(jsonPath("$.lastGroupPurchaseDate").exists())
                .andExpect(jsonPath("$.groupPurchase").value(first.getGroupPurchase()))
                .andExpect(jsonPath("$.groupFrequency").value(first.getGroupFrequency()))
                .andExpect(jsonPath("$.groupMinDiscount").value(first.getGroupMinDiscount()));
    }

    @Test
    void findNotExistingPeriod() throws Exception {
        mockMvc.perform(get("/api/v1/views/periods/{customer_id}/{group_id}",
                        NOT_EXISTING_PERIOD_VIEW_ID.getCustomerId(), NOT_EXISTING_PERIOD_VIEW_ID.getSkuGroupId()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(NOT_FOUND.name()))
                .andExpect(jsonPath("$.message").exists());
    }

    private CustomerDto getCustomerDtoWithId(Long id) {
        return CustomerDto.builder().id(id).build();
    }

    private SkuGroupDto getSkuGroupDtoWithId(Long id) {
        return SkuGroupDto.builder().id(id).build();
    }
}