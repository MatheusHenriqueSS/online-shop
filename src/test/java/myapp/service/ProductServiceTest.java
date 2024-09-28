package myapp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    public static Product createBaseProduct(
    ) {
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
}