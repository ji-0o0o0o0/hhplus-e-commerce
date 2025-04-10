package kr.hhplus.be.server.domain.coupon;

import java.util.Optional;

public interface UserCouponRepository {
    Optional<UserCoupon> findByUserCouponId(Long userCouponId);
}
