package uy.lstabile.paymentchain.customerapi;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.EntityModel;
import org.springframework.test.web.servlet.MockMvc;

import uy.lstabile.paymentchain.customerapi.api.model.CustomerModelAssembler;
import uy.lstabile.paymentchain.customerapi.controller.CustomerController;
import uy.lstabile.paymentchain.customerapi.entities.Customer;
import uy.lstabile.paymentchain.customerapi.repository.CustomerRepository;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.Arrays;

@WebMvcTest(CustomerController.class)
class CustomerControllerUnitTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerRepository repository;

    @MockBean
    private CustomerModelAssembler assembler;

    private static Customer customer = null;

    @BeforeAll
    public static void init() {
        customer = new Customer("Luis", "098823864");
    }

    @Test
    void getAllCustomersTest() throws Exception {

        when(repository.findAll()).thenReturn(
                Arrays.asList(customer));

        when(assembler.toModel(customer)).thenReturn(
            EntityModel.of(customer)
        );

        mockMvc.perform(get("/api/customers/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath(
                        "_embedded.customerList[0].id", is(0)))
                .andExpect(jsonPath("_embedded.customerList[0].name").value("Luis"));
    }
}
