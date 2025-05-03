package kr.hhplus.be.server.interfaces.order;

import kr.hhplus.be.server.application.order.OrderCommand;
import kr.hhplus.be.server.application.order.OrderItemCommand;


import java.util.List;

public record OrderRequest(
        Long userId,
        Long couponId,
        List<OrderItemRequest> items
) {
    // toCommand() 메서드는 interface DTO를 application 계층 Command로 변환합니다.
    public OrderCommand toCommand() {
        List<OrderItemCommand> itemCommands = items.stream()
                .map(item -> new OrderItemCommand(item.productId(), (long) item.quantity()))
                .toList();
        return new OrderCommand(userId, couponId, itemCommands);
    }
}