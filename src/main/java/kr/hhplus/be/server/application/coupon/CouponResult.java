package kr.hhplus.be.server.application.coupon;

import kr.hhplus.be.server.domain.coupon.UserCoupon;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderStatus;

import java.util.List;

public record CouponResult(Long userId, List<UserCoupon> userCoupons) {
    public static CouponResult from(Long userId,List<UserCoupon> userCoupons) {
        return new CouponResult(userId, userCoupons);
    }
}