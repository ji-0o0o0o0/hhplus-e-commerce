package kr.hhplus.be.server.infrastructure.coupon;

import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.CouponRepository;
import kr.hhplus.be.server.domain.coupon.UserCoupon;
import kr.hhplus.be.server.domain.coupon.UserCouponRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class InMemoryCouponRepository implements CouponRepository, UserCouponRepository {


    @Override
    public Optional<Coupon> findByCouponId(Long couponId) {
        return Optional.empty();
    }

    @Override
    public Optional<UserCoupon> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public boolean existsByUserIdAndCouponId(Long userId, Long couponId) {
        return false;
    }

    @Override
    public List<UserCoupon> findAllByUserId(Long userId) {
        return null;
    }

    @Override
    public void save(UserCoupon userCoupon) {

    }

    @Override
    public Optional<UserCoupon> findCouponByUserIdAndCouponId(Long id, Long couponId) {
        return Optional.empty();
    }
}