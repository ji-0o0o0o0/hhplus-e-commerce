package kr.hhplus.be.server.domain.coupon;

import kr.hhplus.be.server.domain.user.User;

import java.util.Optional;

public interface CouponRepository {
    Optional<Coupon> findByCouponId(Long couponId);

}
