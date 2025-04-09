package kr.hhplus.be.server.interfaces.order;

public record OrderItemRequest(
        Long productId,
        int quantity
) {}