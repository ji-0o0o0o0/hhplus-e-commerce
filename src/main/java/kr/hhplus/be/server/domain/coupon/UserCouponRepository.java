package kr.hhplus.be.server.domain.coupon;

import java.util.List;
import java.util.Optional;

public interface UserCouponRepository {

    boolean existsByUserIdAndCouponId(Long userId, Long couponId);

    List<UserCoupon> findAllByUserId(Long userId);

    void save(UserCoupon userCoupon);

    Optional<UserCoupon> findCouponByUserIdAndCouponId(Long id, Long couponId);
}
