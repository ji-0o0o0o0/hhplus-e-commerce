package kr.hhplus.be.server.domain.order;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    @Test
    @DisplayName("쿠폰 없이 총 금액 계산 테스트")
    void calculateTotalAmount_withoutCoupon() {
        // Arrange
        List<OrderItem> items = List.of(new OrderItem(1L, 1000, 2));
        Order order = new Order(1L, items, null, null);

        // Act + Assert
        assertEquals(2000, order.getTotalAmount());
    }

    @Test
    @DisplayName("RATE 쿠폰 적용 시 총 금액 할인 테스트")
    void calculateTotalAmount_withRateCoupon() {
        List<OrderItem> items = List.of(new OrderItem(1L, 1000, 2)); // 2000원
        Order order = new Order(1L, items, 99L, Map.of("RATE", 10.0)); // 10% 할인

        assertEquals(1800, order.getTotalAmount());
    }

    @Test
    @DisplayName("AMOUNT 쿠폰 적용 시 총 금액 할인 테스트")
    void calculateTotalAmount_withAmountCoupon() {
        List<OrderItem> items = List.of(new OrderItem(1L, 1000, 2)); // 2000원
        Order order = new Order(1L, items, 99L, Map.of("AMOUNT", 500.0)); // 500원 할인

        assertEquals(1500, order.getTotalAmount());
    }

    @Test
    @DisplayName("할인 값이 총 금액보다 클 경우 0원 처리")
    void calculateTotalAmount_discountTooBig_returnsZero() {
        List<OrderItem> items = List.of(new OrderItem(1L, 1000, 1)); // 1000원
        Order order = new Order(1L, items, 99L, Map.of("AMOUNT", 2000.0)); // 2000원 할인

        assertEquals(0, order.getTotalAmount());
    }

    @Test
    @DisplayName("알 수 없는 쿠폰 타입이면 할인 없이 원금 유지")
    void calculateTotalAmount_withUnknownCouponType_returnsOriginal() {
        List<OrderItem> items = List.of(new OrderItem(1L, 1000, 1));
        Order order = new Order(1L, items, 99L, Map.of("UNKNOWN", 50.0));

        assertEquals(1000, order.getTotalAmount()); // 할인 없음
    }
}
