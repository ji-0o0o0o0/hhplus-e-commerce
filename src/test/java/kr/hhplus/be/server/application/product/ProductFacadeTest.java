package kr.hhplus.be.server.application.product;

import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

class ProductFacadeTest {

    private ProductService productService;
    private ProductFacade productFacade;

    @BeforeEach
    void setUp() {
        productService = mock(ProductService.class);
        productFacade = new ProductFacade(productService);
    }

    @Test
    @DisplayName("전체 상품 목록 조회 성공")
    void getAllProducts_success() {
        //Arrange
        List<Product> products = List.of(
                new Product(1L, "키보드", 50000, 10),
                new Product(2L, "마우스", 30000, 20)
        );
        when(productService.findAll()).thenReturn(products);
        //Act
        List<ProductResult> results = productFacade.getAllProducts();
        //Assert
        assertEquals(2, results.size());
        assertEquals("키보드", results.get(0).name());
        verify(productService).findAll();
    }
}
