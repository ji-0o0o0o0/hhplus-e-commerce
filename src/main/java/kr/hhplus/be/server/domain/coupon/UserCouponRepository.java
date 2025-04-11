package kr.hhplus.be.server.domain.coupon;

import java.util.Optional;

public interface UserCouponRepository {

    Optional<UserCoupon> findByUserIdAndUserCouponId(Long userId, Long userCouponId);

    void updateIsUsed(Long userCouponId, boolean isUsed);
}
