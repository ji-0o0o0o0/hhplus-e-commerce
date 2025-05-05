package kr.hhplus.be.server.product;


import kr.hhplus.be.server.common.exception.ApiException;
import kr.hhplus.be.server.domain.product.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Product 도메인 단위 테스트")
class ProductTest {

    @Test
    @DisplayName("재고 감소 성공")
    void decreaseStock_success() {
        Product product = Product.create("상품1", "설명", BigDecimal.valueOf(1000), 5L);

        product.decreaseStock(3L);

        assertEquals(2L, product.getStock());
    }

    @Test
    @DisplayName("재고보다 많이 요청 시 예외")
    void decreaseStock_exceedsStock_throwsException() {
        Product product = Product.create("상품1", "설명", BigDecimal.valueOf(1000), 2L);

        assertThrows(ApiException.class, () -> product.decreaseStock(3L));
    }

    @Test
    @DisplayName("재고 복원")
    void restoreStock_success() {
        Product product = Product.create("상품1", "설명", BigDecimal.valueOf(1000), 2L);

        product.increaseStock(3L);

        assertEquals(5L, product.getStock());
    }
}