package kr.hhplus.be.server.order.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record OrderCreateRequest(
        @NotNull @Min(1) Long userId,
        @Min(1) Long userCouponId, // Optional이지만 음수는 막자!
        @NotNull @Size(min = 1) List<@Valid OrderItemRequest> orderItems
) { }