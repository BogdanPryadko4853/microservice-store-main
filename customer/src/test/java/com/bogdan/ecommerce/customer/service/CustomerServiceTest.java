package com.bogdan.ecommerce.customer.service;

import com.bogdan.ecommerce.customer.entity.Address;
import com.bogdan.ecommerce.customer.entity.Customer;
import com.bogdan.ecommerce.customer.entity.CustomerRequest;
import com.bogdan.ecommerce.customer.entity.CustomerResponse;
import com.bogdan.ecommerce.customer.mapper.CustomerMapper;
import com.bogdan.ecommerce.customer.repository.CustomerRepository;
import com.bogdan.ecommerce.exception.CustomerNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerMapper customerMapper;

    @InjectMocks
    private CustomerService customerService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateCustomer() {
        // Arrange
        Address address = new Address("Main Street", "123", "12345");
        CustomerRequest request = new CustomerRequest("1", "John", "Doe", "john.doe@example.com", address);
        Customer customer = Customer.builder()
                .id("1")
                .firstname("John")
                .lastname("Doe")
                .email("john.doe@example.com")
                .address(address)
                .build();

        when(customerMapper.toCustomer(request)).thenReturn(customer);
        when(customerRepository.save(customer)).thenReturn(customer);

        // Act
        String customerId = customerService.createCustomer(request);

        // Assert
        assertEquals("1", customerId);
        verify(customerRepository, times(1)).save(customer);
    }

    @Test
    public void testUpdateCustomer() {
        // Arrange
        Address address = new Address("Main Street", "123", "12345");
        CustomerRequest request = new CustomerRequest("1", "John", "Doe", "john.doe@example.com", address);
        Customer customer = Customer.builder()
                .id("1")
                .firstname("John")
                .lastname("Doe")
                .email("john.doe@example.com")
                .address(address)
                .build();

        when(customerRepository.findById("1")).thenReturn(Optional.of(customer));
        when(customerRepository.save(customer)).thenReturn(customer);

        // Act
        customerService.updateCustomer(request);

        // Assert
        verify(customerRepository, times(1)).save(customer);
    }

    @Test
    public void testUpdateCustomer_CustomerNotFound() {
        // Arrange
        Address address = new Address("Main Street", "123", "12345");
        CustomerRequest request = new CustomerRequest("1", "John", "Doe", "john.doe@example.com", address);

        when(customerRepository.findById("1")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(CustomerNotFoundException.class, () -> customerService.updateCustomer(request));
    }

    @Test
    public void testFindAllCustomers() {
        // Arrange
        Address address = new Address("Main Street", "123", "12345");
        Customer customer1 = Customer.builder()
                .id("1")
                .firstname("John")
                .lastname("Doe")
                .email("john.doe@example.com")
                .address(address)
                .build();
        Customer customer2 = Customer.builder()
                .id("2")
                .firstname("Jane")
                .lastname("Doe")
                .email("jane.doe@example.com")
                .address(address)
                .build();

        when(customerRepository.findAll()).thenReturn(List.of(customer1, customer2));
        when(customerMapper.fromCustomer(customer1)).thenReturn(new CustomerResponse("1", "John", "Doe", "john.doe@example.com", address));
        when(customerMapper.fromCustomer(customer2)).thenReturn(new CustomerResponse("2", "Jane", "Doe", "jane.doe@example.com", address));

        // Act
        List<CustomerResponse> customers = customerService.findAllCustomers();

        // Assert
        assertEquals(2, customers.size());
        assertEquals("1", customers.get(0).id());
        assertEquals("2", customers.get(1).id());
    }

    @Test
    public void testFindById() {
        // Arrange
        Address address = new Address("Main Street", "123", "12345");
        Customer customer = Customer.builder()
                .id("1")
                .firstname("John")
                .lastname("Doe")
                .email("john.doe@example.com")
                .address(address)
                .build();

        when(customerRepository.findById("1")).thenReturn(Optional.of(customer));
        when(customerMapper.fromCustomer(customer)).thenReturn(new CustomerResponse("1", "John", "Doe", "john.doe@example.com", address));

        // Act
        CustomerResponse response = customerService.findById("1");

        // Assert
        assertEquals("1", response.id());
        assertEquals("John", response.firstname());
        assertEquals("Doe", response.lastname());
        assertEquals("john.doe@example.com", response.email());
        assertEquals(address, response.address());
    }

    @Test
    public void testFindById_CustomerNotFound() {
        // Arrange
        when(customerRepository.findById("1")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(CustomerNotFoundException.class, () -> customerService.findById("1"));
    }

    @Test
    public void testExistsById() {
        // Arrange
        when(customerRepository.findById("1")).thenReturn(Optional.of(new Customer()));

        // Act
        boolean exists = customerService.existsById("1");

        // Assert
        assertTrue(exists);
    }

    @Test
    public void testExistsById_CustomerNotFound() {
        // Arrange
        when(customerRepository.findById("1")).thenReturn(Optional.empty());

        // Act
        boolean exists = customerService.existsById("1");

        // Assert
        assertFalse(exists);
    }

    @Test
    public void testDeleteCustomer() {
        // Arrange
        doNothing().when(customerRepository).deleteById("1");

        // Act
        customerService.deleteCustomer("1");

        // Assert
        verify(customerRepository, times(1)).deleteById("1");
    }
}