package kr.hhplus.be.server.application.order;

import kr.hhplus.be.server.domain.order.Order;

public record OrderResult(
        Long orderId
) {   public static OrderResult from(Order order) {
    return new OrderResult(order.getId());
}}
