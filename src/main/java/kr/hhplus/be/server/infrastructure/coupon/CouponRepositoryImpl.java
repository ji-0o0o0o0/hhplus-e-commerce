package kr.hhplus.be.server.infrastructure.coupon;

import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CouponRepositoryImpl implements CouponRepository {
    private final CouponJpaRepository couponJpaRepository;
    @Override
    public Optional<Coupon> findByCouponId(Long couponId) {
        return couponJpaRepository.findById(couponId);
    }

    @Override
    public Coupon save(Coupon coupon) {
        couponJpaRepository.save(coupon);
        return coupon;
    }
}
