package kr.hhplus.be.server.application.order;

import java.util.List;
//주문 단위 요청
public record OrderCommand(
    Long userId,
    Long userCouponId,
    List<OrderItemCommand> orderItems
){
}
