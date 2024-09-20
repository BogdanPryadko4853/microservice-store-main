package com.bogdan.ecommerce.customer.controller;

import com.bogdan.ecommerce.customer.entity.Address;
import com.bogdan.ecommerce.customer.entity.CustomerRequest;
import com.bogdan.ecommerce.customer.entity.CustomerResponse;
import com.bogdan.ecommerce.customer.service.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class CustomerControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private CustomerController customerController;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testCreateCustomer() throws Exception {
        // Arrange
        Address address = new Address("Main Street", "123", "12345");
        CustomerRequest request = new CustomerRequest("1", "John", "Doe", "john.doe@example.com", address);
        String customerId = "1";

        when(customerService.createCustomer(request)).thenReturn(customerId);

        // Act & Assert
        mockMvc.perform(post("/api/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string(customerId));

        verify(customerService, times(1)).createCustomer(request);
    }
    @Test
    public void testUpdateCustomer() throws Exception {
        // Arrange
        Address address = new Address("Main Street", "123", "12345");
        CustomerRequest request = new CustomerRequest("1", "John", "Doe", "john.doe@example.com", address);

        doNothing().when(customerService).updateCustomer(request);

        // Act & Assert
        mockMvc.perform(put("/api/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isAccepted());

        verify(customerService, times(1)).updateCustomer(refEq(request));
    }
    @Test
    public void testFindAllCustomers() throws Exception {
        // Arrange
        Address address = new Address("Main Street", "123", "12345");
        CustomerResponse customer1 = new CustomerResponse("1", "John", "Doe", "john.doe@example.com", address);
        CustomerResponse customer2 = new CustomerResponse("2", "Jane", "Doe", "jane.doe@example.com", address);

        when(customerService.findAllCustomers()).thenReturn(List.of(customer1, customer2));

        // Act & Assert
        mockMvc.perform(get("/api/v1/customers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[1].id").value("2"));

        verify(customerService, times(1)).findAllCustomers();
    }

    @Test
    public void testExistsById() throws Exception {
        // Arrange
        String customerId = "1";
        when(customerService.existsById(customerId)).thenReturn(true);

        // Act & Assert
        mockMvc.perform(get("/api/v1/customers/exists/{customer-id}", customerId))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        verify(customerService, times(1)).existsById(customerId);
    }

    @Test
    public void testFindById() throws Exception {
        // Arrange
        String customerId = "1";
        Address address = new Address("Main Street", "123", "12345");
        CustomerResponse customer = new CustomerResponse(customerId, "John", "Doe", "john.doe@example.com", address);

        when(customerService.findById(customerId)).thenReturn(customer);

        // Act & Assert
        mockMvc.perform(get("/api/v1/customers/{customer-id}", customerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(customerId))
                .andExpect(jsonPath("$.firstname").value("John"))
                .andExpect(jsonPath("$.lastname").value("Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"))
                .andExpect(jsonPath("$.address.street").value("Main Street"))
                .andExpect(jsonPath("$.address.houseNumber").value("123"))
                .andExpect(jsonPath("$.address.zipCode").value("12345"));

        verify(customerService, times(1)).findById(customerId);
    }

    @Test
    public void testDeleteCustomer() throws Exception {
        // Arrange
        String customerId = "1";

        doNothing().when(customerService).deleteCustomer(customerId);

        // Act & Assert
        mockMvc.perform(delete("/api/v1/customers/{customer-id}", customerId))
                .andExpect(status().isAccepted());

        verify(customerService, times(1)).deleteCustomer(customerId);
    }
}