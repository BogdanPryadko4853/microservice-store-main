package com.bogdan.ecommerce.product;

import com.bogdan.ecommerce.exception.ProductPurchaseException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class ProductServiceTest {

    @Mock
    private ProductRepository repository;

    @Mock
    private ProductMapper mapper;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void TestCreateProduct_WithValidRequest_ShouldReturnProductId() {
        // Arrange
        ProductRequest productRequest = new ProductRequest(1, "meat", "angus", 4, BigDecimal.valueOf(1000), 1);
        Product product = Product.builder()
                .id(1)
                .name("meat")
                .description("angus")
                .availableQuantity(4)
                .price(BigDecimal.valueOf(1000))
                .build();

        when(mapper.toProduct(productRequest)).thenReturn(product);
        when(repository.save(product)).thenReturn(product);

        // Act
        Integer productId = productService.createProduct(productRequest);

        // Assert
        assertEquals(1, productId);
    }

    @Test
    public void TestFindById_WithExistingProduct_ShouldReturnProductResponse() {
        // Arrange
        Integer productId = 1;
        Product product = Product.builder()
                .id(productId)
                .name("meat")
                .description("angus")
                .availableQuantity(4)
                .price(BigDecimal.valueOf(1000))
                .build();
        ProductResponse productResponse = new ProductResponse(
                productId, "meat", "angus", 4, BigDecimal.valueOf(1000), 1, "sweet", "all candy you want"
        );

        when(repository.findById(productId)).thenReturn(java.util.Optional.of(product));
        when(mapper.toProductResponse(product)).thenReturn(productResponse);

        // Act
        ProductResponse result = productService.findById(productId);

        // Assert
        assertEquals(productResponse, result);
    }

    @Test
    public void TestFindById_WithNonExistingProduct_ShouldThrowEntityNotFoundException() {
        // Arrange
        Integer productId = 1;
        when(repository.findById(productId)).thenReturn(java.util.Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> productService.findById(productId));
    }

    @Test
    public void TestFindAll_WithExistingProducts_ShouldReturnListOfProductResponses() {
        // Arrange
        Product product1 = Product.builder()
                .id(1)
                .name("meat")
                .description("angus")
                .availableQuantity(4)
                .price(BigDecimal.valueOf(1000))
                .build();
        Product product2 = Product.builder()
                .id(2)
                .name("cheese")
                .description("cheddar")
                .availableQuantity(5)
                .price(BigDecimal.valueOf(500))
                .build();
        List<Product> products = List.of(product1, product2);

        ProductResponse productResponse1 = new ProductResponse(
                1, "meat", "angus", 4, BigDecimal.valueOf(1000), 1, "sweet", "all candy you want"
        );
        ProductResponse productResponse2 = new ProductResponse(
                2, "cheese", "cheddar", 5, BigDecimal.valueOf(500), 1, "sweet", "all candy you want"
        );
        List<ProductResponse> productResponses = List.of(productResponse1, productResponse2);

        when(repository.findAll()).thenReturn(products);
        when(mapper.toProductResponse(product1)).thenReturn(productResponse1);
        when(mapper.toProductResponse(product2)).thenReturn(productResponse2);

        // Act
        List<ProductResponse> result = productService.findAll();

        // Assert
        assertEquals(productResponses, result);
    }


    @Test
    public void TestPurchaseProducts_WithValidRequest_ShouldReturnListOfProductPurchaseResponses() {
        // Arrange
        Product product1 = Product.builder()
                .id(1)
                .name("meat")
                .description("angus")
                .availableQuantity(4)
                .price(BigDecimal.valueOf(1000))
                .build();
        Product product2 = Product.builder()
                .id(2)
                .name("cheese")
                .description("cheddar")
                .availableQuantity(5)
                .price(BigDecimal.valueOf(500))
                .build();
        List<Product> products = List.of(product1, product2);

        ProductPurchaseRequest request1 = new ProductPurchaseRequest(1, 2);
        ProductPurchaseRequest request2 = new ProductPurchaseRequest(2, 3);
        List<ProductPurchaseRequest> requests = List.of(request1, request2);

        ProductPurchaseResponse response1 = new ProductPurchaseResponse(1, "meat", "angus", BigDecimal.valueOf(1000), 2);
        ProductPurchaseResponse response2 = new ProductPurchaseResponse(2, "cheese", "cheddar", BigDecimal.valueOf(500), 3);
        List<ProductPurchaseResponse> expectedResponses = List.of(response1, response2);

        when(repository.findAllByIdInOrderById(List.of(1, 2))).thenReturn(products);
        when(mapper.toproductPurchaseResponse(product1, 2)).thenReturn(response1);
        when(mapper.toproductPurchaseResponse(product2, 3)).thenReturn(response2);

        // Act
        List<ProductPurchaseResponse> result = productService.purchaseProducts(requests);

        // Assert
        assertEquals(expectedResponses, result);
        assertEquals(2, product1.getAvailableQuantity());
        assertEquals(2, product2.getAvailableQuantity());
    }

    @Test
    public void TestPurchaseProducts_WithInsufficientStock_ShouldThrowProductPurchaseException() {
        // Arrange
        Product product1 = Product.builder()
                .id(1)
                .name("meat")
                .description("angus")
                .availableQuantity(1)
                .price(BigDecimal.valueOf(1000))
                .build();
        List<Product> products = List.of(product1);

        ProductPurchaseRequest request1 = new ProductPurchaseRequest(1, 2);
        List<ProductPurchaseRequest> requests = List.of(request1);

        when(repository.findAllByIdInOrderById(List.of(1))).thenReturn(products);

        // Act & Assert
        assertThrows(ProductPurchaseException.class, () -> productService.purchaseProducts(requests));
    }

    @Test
    public void TestPurchaseProducts_WithNonExistingProduct_ShouldThrowProductPurchaseException() {
        // Arrange
        List<ProductPurchaseRequest> requests = List.of(new ProductPurchaseRequest(1, 2));

        when(repository.findAllByIdInOrderById(List.of(1))).thenReturn(List.of());

        // Act & Assert
        assertThrows(ProductPurchaseException.class, () -> productService.purchaseProducts(requests));
    }

}