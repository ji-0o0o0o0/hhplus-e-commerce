package kr.hhplus.be.server.domain.coupon;

import java.time.LocalDate;

public class CouponService {

    private final CouponRepository couponRepository;

    public CouponService(CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    public Coupon getValidCoupon(Long couponId, LocalDate today) {
        Coupon coupon = couponRepository.findByCouponId(couponId)
                .orElseThrow(() -> new IllegalArgumentException("쿠폰이 존재하지 않습니다."));

        if (coupon.isExpired(today)) {
            throw new IllegalArgumentException("쿠폰 유효기간이 지났습니다.");
        }

        return coupon;
    }
}
