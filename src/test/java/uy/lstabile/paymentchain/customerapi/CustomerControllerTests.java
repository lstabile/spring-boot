package uy.lstabile.paymentchain.customerapi;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import uy.lstabile.paymentchain.customerapi.entities.Customer;

import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest()
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = Replace.ANY)
class CustomerControllerTests {

    @Autowired
    private MockMvc mockMvc;

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void getListTest() throws Exception {
        mockMvc.perform(
                post("/api/customers/")
                        .content(asJsonString(new Customer("name", "phone")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());

        mockMvc.perform(
                get("/api/customers/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("_embedded.customerList[0].id").exists());
    }

    @Test
    public void getTest() throws Exception {

        mockMvc.perform(get("/api/customers/{id}", 1)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    public void getWithCustomerNotFoundExceptionTest() throws Exception {

        long id = 99;

        mockMvc.perform(get("/api/customers/{id}", id)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void saveTest() throws Exception {

        mockMvc.perform(
                post("/api/customers/")
                        .content(asJsonString(new Customer("name", "phone")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists());

    }

    @Test
    public void updateTest() throws Exception {

        Customer customer = new Customer("name2", "phone2");

        mockMvc.perform(
                put("/api/customers/{id}", 1)
                        .content(asJsonString(customer))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value(customer.getName()))
                .andExpect(jsonPath("$.phone").value(customer.getPhone()));

    }

    @Test
    public void deleteTest() throws Exception {

        mockMvc.perform(delete("/api/customers/{id}", 1)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    public void deleteWithCustomerNotFoundExceptionTest() throws Exception {

        long id = 99;

        mockMvc.perform(delete("/api/customers/{id}", id)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

}
