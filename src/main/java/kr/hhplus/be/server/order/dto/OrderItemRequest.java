package kr.hhplus.be.server.order.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record OrderItemRequest(
        @NotNull @Min(1) Long productId,
        @NotNull @Min(1) Integer quantity
) { }