package kr.hhplus.be.server.domain.product;

import kr.hhplus.be.server.domain.product.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    @Test
    @DisplayName("재고 차감 성공")
    void deductStock_success() {
        // Arrange
        Product product = new Product(1L, "상품1", 1000, 10);

        // Act
        Product updated = product.deductStock(3);

        // Assert
        assertEquals(7, updated.getStock());
    }

    @Test
    @DisplayName("재고 차감시 재고 부족하면 예외 발생")
    void deductStock_insufficient_throwsException() {
        // Arrange
        Product product = new Product(1L, "상품1", 1000, 2);

        // Act & Assert
        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> product.deductStock(3));
        assertEquals("재고가 부족합니다.", ex.getMessage());
    }

    @Test
    @DisplayName("재고 롤백 성공")
    void rollbackStock_success() {
        // Arrange
        Product product = new Product(1L, "상품1", 1000, 5);

        // Act
        Product rolledBack = product.rollbackStock(2);

        // Assert
        assertEquals(7, rolledBack.getStock());
    }

    @Test
    @DisplayName("롤백되는 수량 0 이하 시 예외")
    void rollbackStock_zeroOrNegative_throwsException() {
        // Arrange
        Product product = new Product(1L, "상품1", 1000, 5);

        // Act & Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> product.rollbackStock(0));
        assertEquals("롤백 수량은 0보다 커야 합니다.", ex.getMessage());
    }
}
