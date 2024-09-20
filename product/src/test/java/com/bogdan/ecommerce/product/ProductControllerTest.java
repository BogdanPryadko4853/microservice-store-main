package com.bogdan.ecommerce.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void TestCreateProduct_WithValidRequest_ShouldReturnProductId() throws Exception {
        // Arrange
        ProductRequest productRequest = new ProductRequest(1, "meat", "angus", 4, BigDecimal.valueOf(1000), 1);
        when(productService.createProduct(any(ProductRequest.class))).thenReturn(1);

        // Act & Assert
        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));
    }

    @Test
    public void TestPurchaseProducts_WithValidRequest_ShouldReturnProductPurchaseResponses() throws Exception {
        // Arrange
        ProductPurchaseRequest request1 = new ProductPurchaseRequest(1, 2);
        ProductPurchaseRequest request2 = new ProductPurchaseRequest(2, 3);
        List<ProductPurchaseRequest> requests = List.of(request1, request2);

        ProductPurchaseResponse response1 = new ProductPurchaseResponse(1, "meat", "angus", BigDecimal.valueOf(1000), 2);
        ProductPurchaseResponse response2 = new ProductPurchaseResponse(2, "cheese", "cheddar", BigDecimal.valueOf(500), 3);
        List<ProductPurchaseResponse> responses = List.of(response1, response2);

        when(productService.purchaseProducts(any(List.class))).thenReturn(responses);

        // Act & Assert
        mockMvc.perform(post("/api/v1/products/purchase")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requests)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(responses)));
    }

    @Test
    public void TestFindById_WithExistingProduct_ShouldReturnProductResponse() throws Exception {
        // Arrange
        Integer productId = 1;
        ProductResponse productResponse = new ProductResponse(
                productId, "meat", "angus", 4, BigDecimal.valueOf(1000), 1, "sweet", "all candy you want"
        );

        when(productService.findById(productId)).thenReturn(productResponse);

        // Act & Assert
        mockMvc.perform(get("/api/v1/products/{product-id}", productId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(productResponse)));
    }

    @Test
    public void TestFindAll_WithExistingProducts_ShouldReturnListOfProductResponses() throws Exception {
        // Arrange
        ProductResponse productResponse1 = new ProductResponse(
                1, "meat", "angus", 4, BigDecimal.valueOf(1000), 1, "sweet", "all candy you want"
        );
        ProductResponse productResponse2 = new ProductResponse(
                2, "cheese", "cheddar", 5, BigDecimal.valueOf(500), 1, "sweet", "all candy you want"
        );
        List<ProductResponse> productResponses = List.of(productResponse1, productResponse2);

        when(productService.findAll()).thenReturn(productResponses);

        // Act & Assert
        mockMvc.perform(get("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(productResponses)));
    }
}