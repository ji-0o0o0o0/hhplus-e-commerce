package kr.hhplus.be.server.infrastructure.coupon;

import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.CouponRepository;
import kr.hhplus.be.server.domain.coupon.UserCoupon;
import kr.hhplus.be.server.domain.coupon.UserCouponRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class InMemoryCouponRepository implements CouponRepository, UserCouponRepository  {

    @Override
    public Optional<Coupon> findByCouponId(Long couponId) {
        return Optional.empty();
    }

    @Override
    public Optional<UserCoupon> findByUserCouponId(Long userCouponId) {
        return Optional.empty();
    }
}