package kr.hhplus.be.server.application.coupon;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class CouponCommand {
    public record Request(
            @NotNull(message = "userId는 필수입니다.")
            @Positive(message = "userId는 0보다 커야 합니다.")
            Long userId,

            Long couponId
    ) {}
}
