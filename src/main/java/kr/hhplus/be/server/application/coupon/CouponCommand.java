package kr.hhplus.be.server.application.coupon;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import kr.hhplus.be.server.domain.coupon.UserCoupon;

import java.util.List;

public class CouponCommand {
    public record IssueRequest(
            @NotNull(message = "userId는 필수입니다.")
            @Positive(message = "userId는 0보다 커야 합니다.")
            Long userId,

            Long couponId
    ) {

        public static CouponCommand.IssueRequest from( Long userId,Long couponId) {
            return new CouponCommand.IssueRequest(userId, couponId);
        }
    }
}
