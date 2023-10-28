package ru.school.retailanalitycs_web_java.controllers.viewControllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import ru.school.retailanalitycs_web_java.IntegrationTestBase;
import ru.school.retailanalitycs_web_java.IntegrationsTestConfiguration;
import ru.school.retailanalitycs_web_java.dto.entityDto.customerDto.CustomerDto;
import ru.school.retailanalitycs_web_java.dto.viewDto.CustomerViewDto;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.school.retailanalitycs_web_java.exceptions.ExceptionCode.NOT_FOUND;

@SpringBootTest(classes = IntegrationsTestConfiguration.class)
@AutoConfigureMockMvc
class CustomerViewControllerTest extends IntegrationTestBase {
    private static final Integer CUSTOMER_ID = 1;
    private static final Integer NOT_EXISTING_ID = 1000;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void findAllCustomerViews() throws Exception {
        mockMvc.perform(get("/api/v1/views/customers"))
                .andExpect(status().isOk());
    }

    @Test
    void findCustomerViewsBy_page_1_size_2() throws Exception {
        CustomerViewDto first = CustomerViewDto.builder()
                .customer(getCustomerDtoWithId(6L))
                .customerAverageCheck(1054.7548476968293)
                .customerAverageCheckSegment("Medium")
                .customerFrequency(38.32937923440972)
                .customerFrequencySegment("Often")
                .customerInactivePeriod(101.1228125)
                .customerChurnRate(2.6382585504859484)
                .customerChurnSegment("Medium")
                .customerSegment(17)
                .customerPrimaryStore(2L).build();
        CustomerViewDto second = CustomerViewDto.builder()
                .customer(getCustomerDtoWithId(19L))
                .customerAverageCheck(928.802104954375)
                .customerAverageCheckSegment("Medium")
                .customerFrequency(83.65048972800926)
                .customerFrequencySegment("Rarely")
                .customerInactivePeriod(44.09388888888889)
                .customerChurnRate(0.527120510976813)
                .customerChurnSegment("Low")
                .customerSegment(10)
                .customerPrimaryStore(4L).build();

        mockMvc.perform(get("/api/v1/views/customers?page=1&size=2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("content[0].customer.id").value(first.getCustomer().getId()))
                .andExpect(jsonPath("content[0].customerAverageCheck").value(first.getCustomerAverageCheck()))
                .andExpect(jsonPath("content[0].customerAverageCheckSegment").value(first.getCustomerAverageCheckSegment()))
                .andExpect(jsonPath("content[0].customerFrequency").value(first.getCustomerFrequency()))
                .andExpect(jsonPath("content[0].customerFrequencySegment").value(first.getCustomerFrequencySegment()))
                .andExpect(jsonPath("content[0].customerInactivePeriod").value(first.getCustomerInactivePeriod()))
                .andExpect(jsonPath("content[0].customerChurnRate").value(first.getCustomerChurnRate()))
                .andExpect(jsonPath("content[0].customerChurnSegment").value(first.getCustomerChurnSegment()))
                .andExpect(jsonPath("content[0].customerSegment").value(first.getCustomerSegment()))
                .andExpect(jsonPath("content[0].customerPrimaryStore").value(first.getCustomerPrimaryStore()))
                .andExpect(jsonPath("content[1].customer.id").value(second.getCustomer().getId()))
                .andExpect(jsonPath("content[1].customerAverageCheck").value(second.getCustomerAverageCheck()))
                .andExpect(jsonPath("content[1].customerAverageCheckSegment").value(second.getCustomerAverageCheckSegment()))
                .andExpect(jsonPath("content[1].customerFrequency").value(second.getCustomerFrequency()))
                .andExpect(jsonPath("content[1].customerFrequencySegment").value(second.getCustomerFrequencySegment()))
                .andExpect(jsonPath("content[1].customerInactivePeriod").value(second.getCustomerInactivePeriod()))
                .andExpect(jsonPath("content[1].customerChurnRate").value(second.getCustomerChurnRate()))
                .andExpect(jsonPath("content[1].customerChurnSegment").value(second.getCustomerChurnSegment()))
                .andExpect(jsonPath("content[1].customerSegment").value(second.getCustomerSegment()))
                .andExpect(jsonPath("content[1].customerPrimaryStore").value(second.getCustomerPrimaryStore()))

                .andExpect(jsonPath("pageable.pageNumber").value(1))
                .andExpect(jsonPath("pageable.pageSize").value(2))
                .andExpect(jsonPath("$.totalPages").value(6))
                .andExpect(jsonPath("$.totalElements").value(12));
    }

    @Test
    void findCustomerViewById() throws Exception {
        CustomerViewDto customerViewDto = CustomerViewDto.builder()
                .customer(getCustomerDtoWithId(1L))
                .customerAverageCheck(919.2208705121053)
                .customerAverageCheckSegment("Low")
                .customerFrequency(76.7075456871412)
                .customerFrequencySegment("Occasionally")
                .customerInactivePeriod(171.30458333333334)
                .customerChurnRate(2.2332168471667035)
                .customerChurnSegment("Medium")
                .customerSegment(5)
                .customerPrimaryStore(1L).build();
        mockMvc.perform(get("/api/v1/views/customers/{CUSTOMER_ID}", CUSTOMER_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customer.id").value(CUSTOMER_ID))
                .andExpect(jsonPath("$.customerAverageCheck").value(customerViewDto.getCustomerAverageCheck()))
                .andExpect(jsonPath("$.customerAverageCheckSegment").value(customerViewDto.getCustomerAverageCheckSegment()))
                .andExpect(jsonPath("$.customerFrequency").value(customerViewDto.getCustomerFrequency()))
                .andExpect(jsonPath("$.customerFrequencySegment").value(customerViewDto.getCustomerFrequencySegment()))
                .andExpect(jsonPath("$.customerInactivePeriod").value(customerViewDto.getCustomerInactivePeriod()))
                .andExpect(jsonPath("$.customerChurnRate").value(customerViewDto.getCustomerChurnRate()))
                .andExpect(jsonPath("$.customerChurnSegment").value(customerViewDto.getCustomerChurnSegment()))
                .andExpect(jsonPath("$.customerSegment").value(customerViewDto.getCustomerSegment()))
                .andExpect(jsonPath("$.customerPrimaryStore").value(customerViewDto.getCustomerPrimaryStore()));
    }

    @Test
    void findNotExistingCustomerView() throws Exception {
        mockMvc.perform(get("/api/v1/views/customers/{NOT_EXISTING_ID}", NOT_EXISTING_ID))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(NOT_FOUND.name()))
                .andExpect(jsonPath("$.message").exists());
    }

    private CustomerDto getCustomerDtoWithId(Long id) {
        return CustomerDto.builder().id(id).build();
    }
}