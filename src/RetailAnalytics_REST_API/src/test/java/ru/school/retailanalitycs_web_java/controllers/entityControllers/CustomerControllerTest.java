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
import ru.school.retailanalitycs_web_java.dto.entityDto.customerDto.CustomerDto;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.school.retailanalitycs_web_java.exceptions.ExceptionCode.ENTITY_IS_NOT_VALID;
import static ru.school.retailanalitycs_web_java.exceptions.ExceptionCode.NOT_FOUND;

@SpringBootTest(classes = IntegrationsTestConfiguration.class)
@AutoConfigureMockMvc
@Transactional
class CustomerControllerTest extends IntegrationTestBase {
    private static final Long CUSTOMER_ID = 1L;
    private static final Long NOT_EXISTING_ID = 1000L;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void findAllCustomers() throws Exception {
        mockMvc.perform(get("/api/v1/customers"))
                .andExpect(status().isOk());
    }

    @Test
    void findCustomersBy_page_1_size_3() throws Exception {
        CustomerDto first = CustomerDto.builder().id(4L).customerName("Александр").customerSurname("Палкин").customerPrimaryEmail("dddca0jokerifia@gmail.com").customerPrimaryPhone("+74957609115").build();
        CustomerDto second = CustomerDto.builder().id(5L).customerName("Лев").customerSurname("Шменин").customerPrimaryEmail("634mexicaneb01@gmail.com").customerPrimaryPhone("+79003272088").build();
        CustomerDto third = CustomerDto.builder().id(6L).customerName("Войцех").customerSurname("Палкин").customerPrimaryEmail("68lodochnikecafa@ya.ru").customerPrimaryPhone("+79000680819").build();
        mockMvc.perform(get("/api/v1/customers?page=1&size=3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("content[0].id").value(first.getId()))
                .andExpect(jsonPath("content[0].customerName").value(first.getCustomerName()))
                .andExpect(jsonPath("content[0].customerSurname").value(first.getCustomerSurname()))
                .andExpect(jsonPath("content[0].customerPrimaryEmail").value(first.getCustomerPrimaryEmail()))
                .andExpect(jsonPath("content[0].customerPrimaryPhone").value(first.getCustomerPrimaryPhone()))
                .andExpect(jsonPath("content[1].id").value(second.getId()))
                .andExpect(jsonPath("content[1].customerName").value(second.getCustomerName()))
                .andExpect(jsonPath("content[1].customerSurname").value(second.getCustomerSurname()))
                .andExpect(jsonPath("content[1].customerPrimaryEmail").value(second.getCustomerPrimaryEmail()))
                .andExpect(jsonPath("content[1].customerPrimaryPhone").value(second.getCustomerPrimaryPhone()))
                .andExpect(jsonPath("content[2].id").value(third.getId()))
                .andExpect(jsonPath("content[2].customerName").value(third.getCustomerName()))
                .andExpect(jsonPath("content[2].customerSurname").value(third.getCustomerSurname()))
                .andExpect(jsonPath("content[2].customerPrimaryEmail").value(third.getCustomerPrimaryEmail()))
                .andExpect(jsonPath("content[2].customerPrimaryPhone").value(third.getCustomerPrimaryPhone()))
                .andExpect(jsonPath("pageable.pageNumber").value(1))
                .andExpect(jsonPath("pageable.pageSize").value(3))
                .andExpect(jsonPath("$.totalPages").value(7))
                .andExpect(jsonPath("$.totalElements").value(20));
    }

    @Test
    void findCustomerById() throws Exception {
        CustomerDto customerDto = CustomerDto.builder().id(CUSTOMER_ID).customerName("Евгений").customerSurname("Варенников").customerPrimaryEmail("hdfavorgbdb700@gmail.com").customerPrimaryPhone("+74910382990").build();
        mockMvc.perform(get("/api/v1/customers/{CUSTOMER_ID}", CUSTOMER_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(CUSTOMER_ID))
                .andExpect(jsonPath("$.customerName").value(customerDto.getCustomerName()))
                .andExpect(jsonPath("$.customerSurname").value(customerDto.getCustomerSurname()))
                .andExpect(jsonPath("$.customerPrimaryEmail").value(customerDto.getCustomerPrimaryEmail()))
                .andExpect(jsonPath("$.customerPrimaryPhone").value(customerDto.getCustomerPrimaryPhone()));
    }

    @Test
    void findNotExistingCustomer() throws Exception {
        mockMvc.perform(get("/api/v1/customers/{NOT_EXISTING_ID}", NOT_EXISTING_ID))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(NOT_FOUND.name()))
                .andExpect(jsonPath("$.messages").exists());
    }

    @Test
    void update() throws Exception {
        CustomerDto customerDto = CustomerDto.builder()
                .customerName("test")
                .customerSurname("test")
                .customerPrimaryEmail("test@mail.ru")
                .customerPrimaryPhone("+74957609115").build();

        String requestJson = objectMapper.writeValueAsString(customerDto);
        mockMvc.perform(put("/api/v1/customers/" + CUSTOMER_ID)
                        .contentType(APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(CUSTOMER_ID))
                .andExpect(jsonPath("$.customerName").value(customerDto.getCustomerName()))
                .andExpect(jsonPath("$.customerSurname").value(customerDto.getCustomerSurname()))
                .andExpect(jsonPath("$.customerPrimaryEmail").value(customerDto.getCustomerPrimaryEmail()))
                .andExpect(jsonPath("$.customerPrimaryPhone").value(customerDto.getCustomerPrimaryPhone()));
    }

    @Test
    void updateNotExistingCustomer_ShouldReturnNotFound() throws Exception {
        CustomerDto customerDto = CustomerDto.builder()
                .customerName("test")
                .customerSurname("test")
                .customerPrimaryEmail("test@mail.ru")
                .customerPrimaryPhone("+74957609115").build();

        String requestJson = objectMapper.writeValueAsString(customerDto);
        mockMvc.perform(put("/api/v1/customers/" + NOT_EXISTING_ID)
                        .contentType(APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(NOT_FOUND.name()))
                .andExpect(jsonPath("$.messages").exists());
    }

    @Test
    void create() throws Exception {
        CustomerDto customerDto = CustomerDto.builder().customerName("test").customerSurname("test").customerPrimaryEmail("test@mail.ru").customerPrimaryPhone("+74957609115").build();

        String requestJson = objectMapper.writeValueAsString(customerDto);
        mockMvc.perform(post("/api/v1/customers")
                        .contentType(APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(21))
                .andExpect(jsonPath("$.customerName").value(customerDto.getCustomerName()))
                .andExpect(jsonPath("$.customerSurname").value(customerDto.getCustomerSurname()))
                .andExpect(jsonPath("$.customerPrimaryEmail").value(customerDto.getCustomerPrimaryEmail()))
                .andExpect(jsonPath("$.customerPrimaryPhone").value(customerDto.getCustomerPrimaryPhone()));
    }

    @Test
    void createWithMissingName_shouldReturnBadRequest() throws Exception {
        CustomerDto customerDto = CustomerDto.builder().customerSurname("test").customerPrimaryEmail("test@mail.ru").customerPrimaryPhone("+74957609115").build();
        String requestJson = objectMapper.writeValueAsString(customerDto);
        mockMvc.perform(post("/api/v1/customers")
                        .contentType(APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ENTITY_IS_NOT_VALID.name()))
                .andExpect(jsonPath("$.messages").exists());
    }

    @Test
    void createWithBlankName_shouldReturnBadRequest() throws Exception {
        CustomerDto customerDto = CustomerDto.builder().customerName(" ").customerSurname("test").customerPrimaryEmail("test@mail.ru").customerPrimaryPhone("+74957609115").build();
        String requestJson = objectMapper.writeValueAsString(customerDto);
        mockMvc.perform(post("/api/v1/customers")
                        .contentType(APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ENTITY_IS_NOT_VALID.name()))
                .andExpect(jsonPath("$.messages").exists());
    }

    @Test
    void createWithMissingSurname_shouldReturnBadRequest() throws Exception {
        CustomerDto customerDto = CustomerDto.builder().customerName("test").customerPrimaryEmail("test@mail.ru").customerPrimaryPhone("+74957609115").build();
        String requestJson = objectMapper.writeValueAsString(customerDto);
        mockMvc.perform(post("/api/v1/customers")
                        .contentType(APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ENTITY_IS_NOT_VALID.name()))
                .andExpect(jsonPath("$.messages").exists());
    }

    @Test
    void createWithBlankSurname_shouldReturnBadRequest() throws Exception {
        CustomerDto customerDto = CustomerDto.builder().customerName("test").customerSurname(" ").customerPrimaryEmail("test@mail.ru").customerPrimaryPhone("+74957609115").build();
        String requestJson = objectMapper.writeValueAsString(customerDto);
        mockMvc.perform(post("/api/v1/customers")
                        .contentType(APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ENTITY_IS_NOT_VALID.name()))
                .andExpect(jsonPath("$.messages").exists());
    }

    @Test
    void createWithMissingEmail_shouldReturnBadRequest() throws Exception {
        CustomerDto customerDto = CustomerDto.builder().customerName("test").customerSurname("test").customerPrimaryPhone("+74957609115").build();
        String requestJson = objectMapper.writeValueAsString(customerDto);
        mockMvc.perform(post("/api/v1/customers")
                        .contentType(APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ENTITY_IS_NOT_VALID.name()))
                .andExpect(jsonPath("$.messages").exists());
    }

    @Test
    void createWithMissingNotValidEmail_shouldReturnBadRequest() throws Exception {
        CustomerDto customerDto = CustomerDto.builder().customerName("test").customerSurname("test").customerPrimaryEmail("NOT_VALID").customerPrimaryPhone("+74957609115").build();
        String requestJson = objectMapper.writeValueAsString(customerDto);
        mockMvc.perform(post("/api/v1/customers")
                        .contentType(APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ENTITY_IS_NOT_VALID.name()))
                .andExpect(jsonPath("$.messages").exists());
    }

    @Test
    void createWithMissingPhone_shouldReturnBadRequest() throws Exception {
        CustomerDto customerDto = CustomerDto.builder().customerName("test").customerSurname("test").customerPrimaryEmail("test@mail.ru").build();
        String requestJson = objectMapper.writeValueAsString(customerDto);
        mockMvc.perform(post("/api/v1/customers")
                        .contentType(APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ENTITY_IS_NOT_VALID.name()))
                .andExpect(jsonPath("$.messages").exists());
    }

    @Test
    void createWithMissingNotValidPhone_shouldReturnBadRequest() throws Exception {
        CustomerDto customerDto = CustomerDto.builder()
                .customerName("test")
                .customerSurname("test")
                .customerPrimaryEmail("NOT_VALID")
                .customerPrimaryEmail("test@mail.ru")
                .customerPrimaryPhone("11111")
                .build();
        String requestJson = objectMapper.writeValueAsString(customerDto);
        mockMvc.perform(post("/api/v1/customers")
                        .contentType(APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ENTITY_IS_NOT_VALID.name()))
                .andExpect(jsonPath("$.messages").exists());
    }

    @Test
    void deleteCustomer() throws Exception {
        mockMvc.perform(delete("/api/v1/customers/{CUSTOMER_ID}", CUSTOMER_ID))
                .andExpect(status().isOk());
        mockMvc.perform(get("/api/v1/customers/{CUSTOMER_ID}", CUSTOMER_ID))
                .andExpect(status().isNotFound());
    }
}