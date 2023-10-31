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
import ru.school.retailanalitycs_web_java.dto.viewDto.GroupViewDto;
import ru.school.retailanalitycs_web_java.entities.tables.Customer;
import ru.school.retailanalitycs_web_java.entities.tables.SkuGroup;
import ru.school.retailanalitycs_web_java.entities.views.groupView.GroupViewId;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.school.retailanalitycs_web_java.exceptions.ExceptionCode.NOT_FOUND;

@SpringBootTest(classes = IntegrationsTestConfiguration.class)
@AutoConfigureMockMvc
class GroupViewControllerTest extends IntegrationTestBase {
    private static final GroupViewId GROUP_VIEW_ID = new GroupViewId(Customer.builder().id(1L).build(), SkuGroup.builder().id(3L).build());
    private static final GroupViewId NOT_EXISTING_GROUP_VIEW_ID = new GroupViewId(Customer.builder().id(Long.MAX_VALUE).build(), SkuGroup.builder().id(Long.MAX_VALUE).build());

    @Autowired
    private MockMvc mockMvc;

    @Test
    void findAllGroups() throws Exception {
        mockMvc.perform(get("/api/v1/views/groups"))
                .andExpect(status().isOk());
    }

    @Test
    void findGroupsBy_page_1_size_2() throws Exception {
        GroupViewDto first = GroupViewDto.builder()
                .customer(getCustomerDtoWithId(19L))
                .skuGroup(getSkuGroupDtoWithId(2L))
                .groupAffinityIndex(0.6)
                .groupChurnRate(4.988283157268143)
                .groupStabilityIndex(0.49704692087270047)
                .groupMargin(-10.602518282680549)
                .groupDiscountShare(0.6666666666666666)
                .groupMinimumDiscount(0.15844591375296724)
                .groupAverageDiscount(0.7917670281294505)
                .build();

        GroupViewDto second = GroupViewDto.builder()
                .customer(getCustomerDtoWithId(1L))
                .skuGroup(getSkuGroupDtoWithId(5L))
                .groupAffinityIndex(0.25)
                .groupChurnRate(1.9261876251429906)
                .groupStabilityIndex(0.9973117806174031)
                .groupMargin(-11.94569463952044)
                .groupDiscountShare(1.0)
                .groupMinimumDiscount(0.12205489263006249)
                .groupAverageDiscount(0.7265296263933619)
                .build();

        mockMvc.perform(get("/api/v1/views/groups?page=1&size=2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("content[0].customer.id").value(first.getCustomer().getId()))
                .andExpect(jsonPath("content[0].skuGroup.id").value(first.getSkuGroup().getId()))
                .andExpect(jsonPath("content[0].groupAffinityIndex").value(first.getGroupAffinityIndex()))
                .andExpect(jsonPath("content[0].groupChurnRate").value(first.getGroupChurnRate()))
                .andExpect(jsonPath("content[0].groupStabilityIndex").value(first.getGroupStabilityIndex()))
                .andExpect(jsonPath("content[0].groupMargin").value(first.getGroupMargin()))
                .andExpect(jsonPath("content[0].groupDiscountShare").value(first.getGroupDiscountShare()))
                .andExpect(jsonPath("content[0].groupMinimumDiscount").value(first.getGroupMinimumDiscount()))
                .andExpect(jsonPath("content[0].groupAverageDiscount").value(first.getGroupAverageDiscount()))
                .andExpect(jsonPath("content[1].customer.id").value(second.getCustomer().getId()))
                .andExpect(jsonPath("content[1].skuGroup.id").value(second.getSkuGroup().getId()))
                .andExpect(jsonPath("content[1].groupAffinityIndex").value(second.getGroupAffinityIndex()))
                .andExpect(jsonPath("content[1].groupChurnRate").value(second.getGroupChurnRate()))
                .andExpect(jsonPath("content[1].groupStabilityIndex").value(second.getGroupStabilityIndex()))
                .andExpect(jsonPath("content[1].groupMargin").value(second.getGroupMargin()))
                .andExpect(jsonPath("content[1].groupDiscountShare").value(second.getGroupDiscountShare()))
                .andExpect(jsonPath("content[1].groupMinimumDiscount").value(second.getGroupMinimumDiscount()))
                .andExpect(jsonPath("content[1].groupAverageDiscount").value(second.getGroupAverageDiscount()))

                .andExpect(jsonPath("pageable.pageNumber").value(1))
                .andExpect(jsonPath("pageable.pageSize").value(2))
                .andExpect(jsonPath("$.totalPages").value(28))
                .andExpect(jsonPath("$.totalElements").value(55));
    }

    @Test
    void findGroupById() throws Exception {
        GroupViewDto first = GroupViewDto.builder()
                .customer(getCustomerDtoWithId(GROUP_VIEW_ID.getCustomer().getId()))
                .skuGroup(getSkuGroupDtoWithId(GROUP_VIEW_ID.getSkuGroup().getId()))
                .groupAffinityIndex(0.38461538461538464)
                .groupChurnRate(1.8290650727814988)
                .groupStabilityIndex(1.1838494338796948)
                .groupMargin(-1724.2867369226256)
                .groupDiscountShare(0.6)
                .groupMinimumDiscount(0.30488100929709167)
                .groupAverageDiscount(0.6845483598552724)
                .build();

        mockMvc.perform(get("/api/v1/views/groups/{customer_id}/{group_id}", GROUP_VIEW_ID.getCustomer().getId(), GROUP_VIEW_ID.getSkuGroup().getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customer.id").value(first.getCustomer().getId()))
                .andExpect(jsonPath("$.skuGroup.id").value(first.getSkuGroup().getId()))
                .andExpect(jsonPath("$.groupAffinityIndex").value(first.getGroupAffinityIndex()))
                .andExpect(jsonPath("$.groupChurnRate").value(first.getGroupChurnRate()))
                .andExpect(jsonPath("$.groupStabilityIndex").value(first.getGroupStabilityIndex()))
                .andExpect(jsonPath("$.groupMargin").value(first.getGroupMargin()))
                .andExpect(jsonPath("$.groupDiscountShare").value(first.getGroupDiscountShare()))
                .andExpect(jsonPath("$.groupMinimumDiscount").value(first.getGroupMinimumDiscount()))
                .andExpect(jsonPath("$.groupAverageDiscount").value(first.getGroupAverageDiscount()));
    }

    @Test
    void findNotExistingGroup() throws Exception {
        mockMvc.perform(get("/api/v1/views/groups/{customer_id}/{group_id}",
                        NOT_EXISTING_GROUP_VIEW_ID.getCustomer().getId(), NOT_EXISTING_GROUP_VIEW_ID.getSkuGroup().getId()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(NOT_FOUND.name()))
                .andExpect(jsonPath("$.messages").exists());
    }

    private CustomerDto getCustomerDtoWithId(Long id) {
        return CustomerDto.builder().id(id).build();
    }

    private SkuGroupDto getSkuGroupDtoWithId(Long id) {
        return SkuGroupDto.builder().id(id).build();
    }
}