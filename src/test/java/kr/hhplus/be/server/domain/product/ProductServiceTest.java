package kr.hhplus.be.server.domain.product;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    private ProductRepository productRepository;
    private ProductService productService;

    @BeforeEach
    void setUp() {
        productRepository = mock(ProductRepository.class);
        productService = new ProductService(productRepository);
    }

    @Test
    @DisplayName("전체 상품 목록 조회")
    void findAllProducts() {
        //Arrange
        List<Product> dummyProducts = List.of(
                new Product(1L, "맥북", 2000000, 5),
                new Product(2L, "아이폰", 1500000, 8)
        );
        when(productRepository.findAll()).thenReturn(dummyProducts);
        //Act
        List<Product> result = productService.findAll();

        //Assert
        assertEquals(2, result.size());
        verify(productRepository).findAll();
    }

    @Test
    @DisplayName("상품 ID로 조회 - 성공")
    void getProductById_success() {
        //Arrange
        Product product = new Product(1L, "아이패드", 1000000, 3);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        //Act
        Product result = productService.getProductById(1L);
        //Assert
        assertEquals(product, result);
    }

    @Test
    @DisplayName("상품 ID로 조회 - 존재하지 않음")
    void getProductById_notFound() {
        //Arrange
        when(productRepository.findById(999L)).thenReturn(Optional.empty());
        //Act+ Assert
        assertThrows(IllegalArgumentException.class, () -> productService.getProductById(999L));
    }

    @Test
    @DisplayName("상품 재고 부족 예외")
    void getProductById_stockEmpty() {
        //Arrange
        Product product = new Product(1L, "에어팟", 300000, 0);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        //Act+ Assert
        assertThrows(IllegalStateException.class, () -> productService.getProductById(1L));
    }
}
