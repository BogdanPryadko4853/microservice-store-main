package com.bogdan.ecommerce.product;

import com.bogdan.ecommerce.category.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ProductMapperTest {

    @InjectMocks
    private ProductMapper productMapper;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    public void TestToProduct_WithValidRequest_ShouldReturnProduct() {
        // Arrange
        ProductRequest productRequest = new ProductRequest(1, "meat", "angus", 4, BigDecimal.valueOf(1000), 1);

        // Act
        Product product = productMapper.toProduct(productRequest);

        // Assert
        assertEquals(1, product.getId());
        assertEquals("meat", product.getName());
        assertEquals("angus", product.getDescription());
        assertEquals(4, product.getAvailableQuantity());
        assertEquals(BigDecimal.valueOf(1000), product.getPrice());

        // Проверка категории
        assertNotNull(product.getCategory());
        assertEquals(1, product.getCategory().getId());
    }
    @Test
    public void TestToProductResponse_WithValidProduct_ShouldReturnProductResponse() {
        // Arrange
        Category category = Category.builder()
                .id(1)
                .name("sweet")
                .description("all candy you want")
                .build();

        Product product = Product.builder()
                .id(1)
                .name("meat")
                .description("angus")
                .availableQuantity(4)
                .price(BigDecimal.valueOf(1000))
                .category(category)
                .build();

        // Act
        ProductResponse productResponse = productMapper.toProductResponse(product);

        // Assert
        assertEquals(1, productResponse.id());
        assertEquals("meat", productResponse.name());
        assertEquals("angus", productResponse.description());
        assertEquals(4, productResponse.availableQuantity());
        assertEquals(BigDecimal.valueOf(1000), productResponse.price());
        assertEquals(1, productResponse.categoryId());
        assertEquals("sweet", productResponse.categoryName());
        assertEquals("all candy you want", productResponse.categoryDescription());
    }
    @Test
    public void TestToProductPurchaseResponse_WithValidProductAndQuantity_ShouldReturnProductPurchaseResponse() {
        // Arrange
        Product product = Product.builder()
                .id(1)
                .name("meat")
                .description("angus")
                .price(BigDecimal.valueOf(1000))
                .build();

        double quantity = 2.5;

        // Act
        ProductPurchaseResponse productPurchaseResponse = productMapper.toproductPurchaseResponse(product, quantity);

        // Assert
        assertEquals(1, productPurchaseResponse.productId());
        assertEquals("meat", productPurchaseResponse.name());
        assertEquals("angus", productPurchaseResponse.description());
        assertEquals(BigDecimal.valueOf(1000), productPurchaseResponse.price());
        assertEquals(2.5, productPurchaseResponse.quantity());
    }






}