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
import ru.school.retailanalitycs_web_java.dto.entityDto.transactionDto.TransactionReadDto;
import ru.school.retailanalitycs_web_java.dto.viewDto.PurchaseHistoryViewDto;
import ru.school.retailanalitycs_web_java.entities.tables.Customer;
import ru.school.retailanalitycs_web_java.entities.tables.Transaction;
import ru.school.retailanalitycs_web_java.entities.views.purchaseHistoryView.PurchaseHistoryViewId;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.school.retailanalitycs_web_java.exceptions.ExceptionCode.NOT_FOUND;

@SpringBootTest(classes = IntegrationsTestConfiguration.class)
@AutoConfigureMockMvc
class PurchaseHistoryViewControllerTest extends IntegrationTestBase {
    private static final PurchaseHistoryViewId PH_VIEW_ID = new PurchaseHistoryViewId(Customer.builder().id(1L).build(), Transaction.builder().id(46L).build());
    private static final PurchaseHistoryViewId NOT_EXISTING_PH_VIEW_ID = new PurchaseHistoryViewId(Customer.builder().id(Long.MAX_VALUE).build(), Transaction.builder().id(Long.MAX_VALUE).build());

    @Autowired
    private MockMvc mockMvc;

    @Test
    void findAllPurchaseHistories() throws Exception {
        mockMvc.perform(get("/api/v1/views/purchase-histories"))
                .andExpect(status().isOk());
    }

    @Test
    void findPurchaseHistoriesBy_page_1_size_2() throws Exception {
        PurchaseHistoryViewDto first = PurchaseHistoryViewDto.builder()
                .customer(getCustomerDtoWithId(1L))
                .transaction(getTransactionReadDtoWithId(46L))
                .skuGroup(SkuGroupDto.builder().id(1L).build())
                .groupCost(492.21883884103266)
                .groupSumm(933.7694265)
                .groupSummPaid(811.3839555)
                .build();

        PurchaseHistoryViewDto second = PurchaseHistoryViewDto.builder()
                .customer(getCustomerDtoWithId(1L))
                .transaction(getTransactionReadDtoWithId(59L))
                .skuGroup(SkuGroupDto.builder().id(2L).build())
                .groupCost(42.366561491139926)
                .groupSumm(45.23478835)
                .groupSummPaid(45.23478835)
                .build();

        mockMvc.perform(get("/api/v1/views/purchase-histories?page=1&size=2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("content[0].customer.id").value(first.getCustomer().getId()))
                .andExpect(jsonPath("content[0].transaction.id").value(first.getTransaction().getId()))
                .andExpect(jsonPath("content[0].transactionDatetime").exists())
                .andExpect(jsonPath("content[0].skuGroup.id").value(first.getSkuGroup().getId()))
                .andExpect(jsonPath("content[0].groupCost").value(first.getGroupCost()))
                .andExpect(jsonPath("content[0].groupSumm").value(first.getGroupSumm()))
                .andExpect(jsonPath("content[0].groupSummPaid").value(first.getGroupSummPaid()))
                .andExpect(jsonPath("content[1].customer.id").value(second.getCustomer().getId()))
                .andExpect(jsonPath("content[1].transaction.id").value(second.getTransaction().getId()))
                .andExpect(jsonPath("content[1].transactionDatetime").exists())
                .andExpect(jsonPath("content[1].skuGroup.id").value(second.getSkuGroup().getId()))
                .andExpect(jsonPath("content[1].groupCost").value(second.getGroupCost()))
                .andExpect(jsonPath("content[1].groupSumm").value(second.getGroupSumm()))
                .andExpect(jsonPath("content[1].groupSummPaid").value(second.getGroupSummPaid()))

                .andExpect(jsonPath("pageable.pageNumber").value(1))
                .andExpect(jsonPath("pageable.pageSize").value(2))
                .andExpect(jsonPath("$.totalPages").value(100))
                .andExpect(jsonPath("$.totalElements").value(200));
    }

    @Test
    void findPurchaseHistoryById() throws Exception {
        PurchaseHistoryViewDto first = PurchaseHistoryViewDto.builder()
                .customer(getCustomerDtoWithId(PH_VIEW_ID.getCustomer().getId()))
                .transaction(getTransactionReadDtoWithId(PH_VIEW_ID.getTransaction().getId()))
                .skuGroup(SkuGroupDto.builder().id(1L).build())
                .groupCost(492.21883884103266)
                .groupSumm(933.7694265)
                .groupSummPaid(811.3839555)
                .build();

        mockMvc.perform(get("/api/v1/views/purchase-histories/{customer_id}/{transaction_id}",
                        PH_VIEW_ID.getCustomer().getId(), PH_VIEW_ID.getTransaction().getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customer.id").value(first.getCustomer().getId()))
                .andExpect(jsonPath("$.transaction.id").value(first.getTransaction().getId()))
                .andExpect(jsonPath("$.transactionDatetime").exists())
                .andExpect(jsonPath("$.skuGroup.id").value(first.getSkuGroup().getId()))
                .andExpect(jsonPath("$.groupCost").value(first.getGroupCost()))
                .andExpect(jsonPath("$.groupSumm").value(first.getGroupSumm()))
                .andExpect(jsonPath("$.groupSummPaid").value(first.getGroupSummPaid()));
    }

    @Test
    void findNotExistingPurchaseHistory() throws Exception {
        mockMvc.perform(get("/api/v1/views/purchase-histories/{customer_id}/{transaction_id}",
                        NOT_EXISTING_PH_VIEW_ID.getCustomer().getId(), NOT_EXISTING_PH_VIEW_ID.getTransaction().getId()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(NOT_FOUND.name()))
                .andExpect(jsonPath("$.messages").exists());
    }

    private CustomerDto getCustomerDtoWithId(Long id) {
        return CustomerDto.builder().id(id).build();
    }

    private TransactionReadDto getTransactionReadDtoWithId(Long id) {
        return TransactionReadDto.builder().id(id).build();
    }
}