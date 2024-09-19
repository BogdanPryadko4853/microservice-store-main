package com.bogdan.ecommerce.customer.mapper;

import com.bogdan.ecommerce.customer.entity.Address;
import com.bogdan.ecommerce.customer.entity.Customer;
import com.bogdan.ecommerce.customer.entity.CustomerRequest;
import com.bogdan.ecommerce.customer.entity.CustomerResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class CustomerMapperTest {

    @InjectMocks
    private CustomerMapper customerMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testToCustomer_WithValidRequest_ShouldReturnCustomer() {
        // Arrange
        Address address = new Address("Main Street", "123", "12345");
        CustomerRequest request = new CustomerRequest("1", "John", "Doe", "john.doe@example.com", address);

        // Act
        Customer customer = customerMapper.toCustomer(request);

        // Assert
        assertEquals("1", customer.getId());
        assertEquals("John", customer.getFirstname());
        assertEquals("Doe", customer.getLastname());
        assertEquals("john.doe@example.com", customer.getEmail());
        assertEquals(address, customer.getAddress());
    }

    @Test
    public void testToCustomer_WithNullRequest_ShouldReturnNull() {
        // Act
        Customer customer = customerMapper.toCustomer(null);

        // Assert
        assertNull(customer);
    }

    @Test
    public void testFromCustomer_WithValidCustomer_ShouldReturnCustomerResponse() {
        // Arrange
        Address address = new Address("Main Street", "123", "12345");
        Customer customer = Customer.builder()
                .id("1")
                .firstname("John")
                .lastname("Doe")
                .email("john.doe@example.com")
                .address(address)
                .build();

        // Act
        CustomerResponse response = customerMapper.fromCustomer(customer);

        // Assert
        assertEquals("1", response.id());
        assertEquals("John", response.firstname());
        assertEquals("Doe", response.lastname());
        assertEquals("john.doe@example.com", response.email());
        assertEquals(address, response.address());
    }

    @Test
    public void testFromCustomer_WithNullCustomer_ShouldReturnNull() {
        // Act
        CustomerResponse response = customerMapper.fromCustomer(null);

        // Assert
        assertNull(response);
    }
}