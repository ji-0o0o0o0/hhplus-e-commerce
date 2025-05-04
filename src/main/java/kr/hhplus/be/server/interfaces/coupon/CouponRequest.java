package kr.hhplus.be.server.interfaces.coupon;


import kr.hhplus.be.server.application.coupon.CouponCommand;

public record CouponRequest(Long userId, Long couponId) {
    public CouponCommand.Request toCommand() {
        return new CouponCommand.Request(userId, couponId);
    }
}
