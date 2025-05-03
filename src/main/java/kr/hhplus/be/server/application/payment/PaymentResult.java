package kr.hhplus.be.server.application.payment;

import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderStatus;

public record PaymentResult(Long orderId, OrderStatus orderStatus) {
    public static PaymentResult from(Order order) {
        return new PaymentResult(order.getId(), order.getStatus());
    }
}
