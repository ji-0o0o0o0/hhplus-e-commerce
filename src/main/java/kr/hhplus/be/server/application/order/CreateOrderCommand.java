package kr.hhplus.be.server.application.order;

import kr.hhplus.be.server.domain.order.OrderItem;

import java.util.List;
//주문 단위 요청
public record  CreateOrderCommand(
    Long userId,
    Long userCouponId,
    List<OrderItemCommand> orderItems
){
}
