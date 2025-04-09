package kr.hhplus.be.server.domain.order;

public interface CouponValidator {
    void validateCouponOwnership(Long userId, Long couponId);
}