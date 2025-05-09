package kr.hhplus.be.server.application.coupon;

import kr.hhplus.be.server.domain.coupon.UserCoupon;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderStatus;

import java.util.List;

public class CouponResult {
    public record Issue(Long userId, Long couponId) {
        public static Issue from(UserCoupon userCoupon) {
            return new Issue(userCoupon.getUserId(), userCoupon.getCouponId());
        }
    }
    public record UserCoupons(Long userId, List<UserCoupon> userCoupons) {
        public static UserCoupons from(Long userId, List<UserCoupon> userCoupons) {
            return new UserCoupons(userId, userCoupons);
        }
    }
}