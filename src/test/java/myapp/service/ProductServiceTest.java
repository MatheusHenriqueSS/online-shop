package myapp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import myapp.domain.Product;
import myapp.domain.enumeration.ProductStatus;
import myapp.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    public static Product createBaseProduct() {
        return new Product()
            .id(1L)
            .title("title")
            .keywords(null)
            .description(null)
            .rating(0)
            .quantityInStock(null)
            .dimensions(null)
            .price(BigDecimal.ZERO)
            .status(ProductStatus.IN_STOCK)
            .weight(null)
            .dateAdded(Instant.now().minus(Duration.ofDays(1)))
            .dateModified(null);
    }

    // Helper methods to create strings of specific lengths
    private String createString(int length) {
        return "a".repeat(length);
    }

    // Helper method to create Instant based on days offset
    private Instant createDate(int daysOffset) {
        return Instant.now().plus(Duration.ofDays(daysOffset));
    }

    // Valid Test Cases

    @Test
    public void testValidTC1() {
        Product product = createBaseProduct();
        product.setTitle(createString(3)); // str(3)
        product.setKeywords(null); // NULL
        product.setDescription(null); // NULL
        product.setRating(null); // NULL
        product.setPrice(new BigDecimal("0.00")); // 0.00
        product.setQuantityInStock(null); // NULL
        product.setStatus(ProductStatus.IN_STOCK); // Disponível
        product.setWeight(null); // NULL
        product.setDimensions(null); // NULL
        product.setDateAdded(Instant.now()); // Now
        product.setDateModified(null); // NULL

        when(productRepository.save(product)).thenReturn(product);
        Product savedProduct = productService.save(product);

        assertEquals(product, savedProduct);
    }

    @Test
    public void testValidTC2() {
        Product product = createBaseProduct();
        product.setTitle(createString(4)); // str(4)
        product.setKeywords(createString(199)); // str(199)
        product.setDescription(createString(10)); // str(10)
        product.setRating(0); // 0
        product.setPrice(new BigDecimal("0.01")); // 0.01
        product.setQuantityInStock(0); // 0
        product.setStatus(ProductStatus.OUT_OF_STOCK); // Esgotado
        product.setWeight(0.00); // 0.00
        product.setDimensions(createString(49)); // str(49)
        product.setDateAdded(createDate(-1)); // Now - 1
        product.setDateModified(product.getDateAdded()); // Data de adição

        when(productRepository.save(product)).thenReturn(product);
        Product savedProduct = productService.save(product);

        assertEquals(product, savedProduct);
    }

    @Test
    public void testValidTC3() {
        Product product = createBaseProduct();
        product.setTitle(createString(99)); // str(99)
        product.setKeywords(createString(200)); // str(200)
        product.setDescription(createString(11)); // str(11)
        product.setRating(1); // 1
        product.setPrice(new BigDecimal("0.01")); // 0.01
        product.setQuantityInStock(1); // 1
        product.setStatus(ProductStatus.DISCONTINUED); // Descontinuado
        product.setWeight(0.00); // 0.01
        product.setDimensions(createString(50)); // str(50)
        product.setDateAdded(createDate(-1)); // Now - 1
        product.setDateModified(product.getDateAdded().plus(Duration.ofDays(1))); // Data de adição + 1

        when(productRepository.save(product)).thenReturn(product);
        Product savedProduct = productService.save(product);

        assertEquals(product, savedProduct);
    }

    @Test
    public void testValidTC4() {
        Product product = createBaseProduct();
        product.setTitle(createString(100)); // str(100)
        product.setKeywords(createString(200)); // str(200)
        product.setDescription(createString(11)); // str(11)
        product.setRating(4); // 4
        product.setPrice(new BigDecimal("0.01")); // 0.01
        product.setQuantityInStock(1); // 1
        product.setStatus(ProductStatus.DISCONTINUED); // Descontinuado
        product.setWeight(0.01); // 0.01
        product.setDimensions(createString(50)); // str(50)
        product.setDateAdded(createDate(-1)); // Now - 1
        product.setDateModified(Instant.now()); // Now

        when(productRepository.save(product)).thenReturn(product);
        Product savedProduct = productService.save(product);

        assertEquals(product, savedProduct);
    }

    @Test
    public void testValidTC5() {
        Product product = createBaseProduct();
        product.setTitle(createString(100)); // str(100)
        product.setKeywords(createString(200)); // str(200)
        product.setDescription(createString(11)); // str(11)
        product.setRating(5); // 5
        product.setPrice(new BigDecimal("0.01")); // 0.01
        product.setQuantityInStock(1); // 1
        product.setStatus(ProductStatus.DISCONTINUED); // Descontinuado
        product.setWeight(0.01); // 0.01
        product.setDimensions(createString(50)); // str(50)
        product.setDateAdded(createDate(-1)); // Now - 1
        product.setDateModified(createDate(-1)); // Now - 1

        when(productRepository.save(product)).thenReturn(product);
        Product savedProduct = productService.save(product);

        assertEquals(product, savedProduct);
    }

    // Invalid Test Cases

    @Test
    public void testInvalidTC1() {
        Product product = createBaseProduct();
        product.setTitle(null); // NULL (Invalid)
        product.setKeywords(null);
        product.setDescription(null);
        product.setRating(null);
        product.setPrice(new BigDecimal("0.00"));
        product.setQuantityInStock(null);
        product.setStatus(ProductStatus.IN_STOCK);
        product.setWeight(null);
        product.setDimensions(null);
        product.setDateAdded(Instant.now());
        product.setDateModified(null);

        assertThrows(Exception.class, () -> {
            productService.save(product);
        });
    }

    @Test
    public void testInvalidTC2() {
        Product product = createBaseProduct();
        product.setTitle(createString(2)); // str(2) (Invalid)
        product.setKeywords(null);
        product.setDescription(null);
        product.setRating(null);
        product.setPrice(new BigDecimal("0.00"));
        product.setQuantityInStock(null);
        product.setStatus(ProductStatus.IN_STOCK);
        product.setWeight(null);
        product.setDimensions(null);
        product.setDateAdded(Instant.now());
        product.setDateModified(null);

        assertThrows(Exception.class, () -> {
            productService.save(product);
        });
    }

    @Test
    public void testInvalidTC3() {
        Product product = createBaseProduct();
        product.setTitle(createString(101)); // str(101) (Invalid)
        product.setKeywords(null);
        product.setDescription(null);
        product.setRating(null);
        product.setPrice(new BigDecimal("0.00"));
        product.setQuantityInStock(null);
        product.setStatus(ProductStatus.IN_STOCK);
        product.setWeight(null);
        product.setDimensions(null);
        product.setDateAdded(Instant.now());
        product.setDateModified(null);

        assertThrows(Exception.class, () -> {
            productService.save(product);
        });
    }

    @Test
    public void testInvalidTC4() {
        Product product = createBaseProduct();
        product.setTitle(createString(3));
        product.setKeywords(createString(201)); // str(201) (Invalid)
        product.setDescription(null);
        product.setRating(null);
        product.setPrice(new BigDecimal("0.00"));
        product.setQuantityInStock(null);
        product.setStatus(ProductStatus.IN_STOCK);
        product.setWeight(null);
        product.setDimensions(null);
        product.setDateAdded(Instant.now());
        product.setDateModified(null);

        assertThrows(Exception.class, () -> {
            productService.save(product);
        });
    }

    @Test
    public void testInvalidTC5() {
        Product product = createBaseProduct();
        product.setTitle(createString(3));
        product.setKeywords(null);
        product.setDescription(createString(9)); // str(9) (Invalid)
        product.setRating(null);
        product.setPrice(new BigDecimal("0.00"));
        product.setQuantityInStock(null);
        product.setStatus(ProductStatus.IN_STOCK);
        product.setWeight(null);
        product.setDimensions(null);
        product.setDateAdded(Instant.now());
        product.setDateModified(null);

        assertThrows(Exception.class, () -> {
            productService.save(product);
        });
    }

    @Test
    public void testInvalidTC6() {
        Product product = createBaseProduct();
        product.setTitle(createString(3));
        product.setKeywords(null);
        product.setDescription(null);
        product.setRating(6); // 6 (Invalid)
        product.setPrice(new BigDecimal("0.00"));
        product.setQuantityInStock(null);
        product.setStatus(ProductStatus.IN_STOCK);
        product.setWeight(null);
        product.setDimensions(null);
        product.setDateAdded(Instant.now());
        product.setDateModified(null);

        assertThrows(Exception.class, () -> {
            productService.save(product);
        });
    }

    @Test
    public void testInvalidTC7() {
        Product product = createBaseProduct();
        product.setTitle(createString(3));
        product.setKeywords(null);
        product.setDescription(null);
        product.setRating(-1); // -1 (Invalid)
        product.setPrice(new BigDecimal("0.00"));
        product.setQuantityInStock(null);
        product.setStatus(ProductStatus.IN_STOCK);
        product.setWeight(null);
        product.setDimensions(null);
        product.setDateAdded(Instant.now());
        product.setDateModified(null);

        assertThrows(Exception.class, () -> {
            productService.save(product);
        });
    }

    @Test
    public void testInvalidTC8() {
        Product product = createBaseProduct();
        product.setTitle(createString(3));
        product.setKeywords(null);
        product.setDescription(null);
        product.setRating(null);
        product.setPrice(null); // NULL (Invalid)
        product.setQuantityInStock(null);
        product.setStatus(ProductStatus.IN_STOCK);
        product.setWeight(null);
        product.setDimensions(null);
        product.setDateAdded(Instant.now());
        product.setDateModified(null);

        assertThrows(Exception.class, () -> {
            productService.save(product);
        });
    }

    @Test
    public void testInvalidTC9() {
        Product product = createBaseProduct();
        product.setTitle(createString(3));
        product.setKeywords(null);
        product.setDescription(null);
        product.setRating(null);
        product.setPrice(new BigDecimal("-0.01")); // -0.01 (Invalid)
        product.setQuantityInStock(null);
        product.setStatus(ProductStatus.IN_STOCK);
        product.setWeight(null);
        product.setDimensions(null);
        product.setDateAdded(Instant.now());
        product.setDateModified(null);

        assertThrows(Exception.class, () -> {
            productService.save(product);
        });
    }

    @Test
    public void testInvalidTC10() {
        Product product = createBaseProduct();
        product.setTitle(createString(3));
        product.setKeywords(null);
        product.setDescription(null);
        product.setRating(null);
        product.setPrice(new BigDecimal("0.00"));
        product.setQuantityInStock(-1); // -1 (Invalid)
        product.setStatus(ProductStatus.IN_STOCK);
        product.setWeight(null);
        product.setDimensions(null);
        product.setDateAdded(Instant.now());
        product.setDateModified(null);

        assertThrows(Exception.class, () -> {
            productService.save(product);
        });
    }

    @Test
    public void testInvalidTC11() {
        Product product = createBaseProduct();
        product.setTitle(createString(3));
        product.setKeywords(null);
        product.setDescription(null);
        product.setRating(null);
        product.setPrice(new BigDecimal("0.00"));
        product.setQuantityInStock(null);
        product.setStatus(null); // NULL (Invalid)
        product.setWeight(null);
        product.setDimensions(null);
        product.setDateAdded(Instant.now());
        product.setDateModified(null);

        assertThrows(Exception.class, () -> {
            productService.save(product);
        });
    }

    @Test
    public void testInvalidTC12() {
        Product product = createBaseProduct();
        product.setTitle(createString(3));
        product.setKeywords(null);
        product.setDescription(null);
        product.setRating(null);
        product.setPrice(new BigDecimal("0.00"));
        product.setQuantityInStock(null);
        product.setStatus(ProductStatus.IN_STOCK);
        product.setWeight(-0.01); // -0.01 (Invalid)
        product.setDimensions(null);
        product.setDateAdded(Instant.now());
        product.setDateModified(null);

        assertThrows(Exception.class, () -> {
            productService.save(product);
        });
    }

    @Test
    public void testInvalidTC13() {
        Product product = createBaseProduct();
        product.setTitle(createString(3));
        product.setKeywords(null);
        product.setDescription(null);
        product.setRating(null);
        product.setPrice(new BigDecimal("0.00"));
        product.setQuantityInStock(null);
        product.setStatus(ProductStatus.IN_STOCK);
        product.setWeight(null);
        product.setDimensions(createString(51)); // str(51) (Invalid)
        product.setDateAdded(Instant.now());
        product.setDateModified(null);

        assertThrows(Exception.class, () -> {
            productService.save(product);
        });
    }

    @Test
    public void testInvalidTC14() {
        Product product = createBaseProduct();
        product.setTitle(createString(3));
        product.setKeywords(null);
        product.setDescription(null);
        product.setRating(null);
        product.setPrice(new BigDecimal("0.00"));
        product.setQuantityInStock(null);
        product.setStatus(ProductStatus.IN_STOCK);
        product.setWeight(null);
        product.setDimensions(null);
        product.setDateAdded(createDate(1)); // Now + 1 (Invalid)
        product.setDateModified(null);

        assertThrows(Exception.class, () -> {
            productService.save(product);
        });
    }

    @Test
    public void testInvalidTC15() {
        Product product = createBaseProduct();
        product.setTitle(createString(3));
        product.setKeywords(null);
        product.setDescription(null);
        product.setRating(null);
        product.setPrice(new BigDecimal("0.00"));
        product.setQuantityInStock(null);
        product.setStatus(ProductStatus.IN_STOCK);
        product.setWeight(null);
        product.setDimensions(null);
        product.setDateAdded(null); // NULL (Invalid)
        product.setDateModified(null);

        assertThrows(Exception.class, () -> {
            productService.save(product);
        });
    }

    @Test
    public void testInvalidTC16() {
        Product product = createBaseProduct();
        product.setTitle(createString(3));
        product.setKeywords(null);
        product.setDescription(null);
        product.setRating(null);
        product.setPrice(new BigDecimal("0.00"));
        product.setQuantityInStock(null);
        product.setStatus(ProductStatus.IN_STOCK);
        product.setWeight(null);
        product.setDimensions(null);
        product.setDateAdded(Instant.now());
        product.setDateModified(createDate(1)); // Now + 1 (Invalid)

        assertThrows(Exception.class, () -> {
            productService.save(product);
        });
    }

    @Test
    public void testInvalidTC17() {
        Product product = createBaseProduct();
        product.setTitle(createString(3));
        product.setKeywords(null);
        product.setDescription(null);
        product.setRating(null);
        product.setPrice(new BigDecimal("0.00"));
        product.setQuantityInStock(null);
        product.setStatus(ProductStatus.IN_STOCK);
        product.setWeight(null);
        product.setDimensions(null);
        product.setDateAdded(Instant.now());
        product.setDateModified(product.getDateAdded().minus(Duration.ofDays(1))); // Data de adição - 1 (Invalid)

        assertThrows(Exception.class, () -> {
            productService.save(product);
        });
    }
}