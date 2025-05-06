package kr.hhplus.be.server.domain.order;

import kr.hhplus.be.server.common.exception.ApiException;
import kr.hhplus.be.server.domain.product.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class OrderProductTest {
    @Test
    @DisplayName("OrderProduct 생성 성공 및 가격 계산")
    void createOrderProduct_success() {
        Product product = Product.create("상품1", "설명", BigDecimal.valueOf(1500), 10L);
        OrderProduct orderProduct = OrderProduct.create(product, 2L);

        assertEquals(2L, orderProduct.getQuantity());
        assertEquals(BigDecimal.valueOf(1500), orderProduct.getPrice());
    }

    @Test
    @DisplayName("수량이 0 이하일 경우 예외 발생")
    void createOrderProduct_invalidQuantity_throwsException() {
        Product product = Product.create("상품1", "설명", BigDecimal.valueOf(1500), 10L);

        assertThrows(ApiException.class, () -> OrderProduct.create(product, 0L));
        assertThrows(ApiException.class, () -> OrderProduct.create(product, -2L));
    }
}
