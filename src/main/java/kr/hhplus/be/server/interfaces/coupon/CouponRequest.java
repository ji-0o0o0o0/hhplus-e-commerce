package kr.hhplus.be.server.interfaces.coupon;


import kr.hhplus.be.server.application.coupon.CouponCommand;

public record CouponRequest(Long userId, Long couponId) {
    public CouponCommand.IssueRequest toCommand() {
        return new CouponCommand.IssueRequest(userId, couponId);
    }
}
