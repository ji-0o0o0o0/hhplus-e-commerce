package kr.hhplus.be.server.application.payment;

import kr.hhplus.be.server.application.coupon.CouponCommand;

public record PaymentCommand(Long userId, Long orderId) {
    public static PaymentCommand from(Long userId, Long orderId) {
        return new PaymentCommand(userId, orderId);
    }
}