package kr.hhplus.be.server.interfaces.coupon;

import kr.hhplus.be.server.domain.coupon.UserCoupon;

import java.time.LocalDate;
import java.util.List;

public record CouponResponse(Long id, String name, boolean isUsed, LocalDate issuedAt, LocalDate expiredAt) {

    public static CouponResponse from(UserCoupon coupon) {
        return new CouponResponse(
                coupon.getId(),
                coupon.getCoupon().getCouponName(),
                coupon.isUsed(),
                coupon.getCoupon().getStartDate(),
                coupon.getCoupon().getEndDate()
        );
    }

    public static List<CouponResponse> listFrom(List<UserCoupon> coupons) {
        return coupons.stream()
                .map(CouponResponse::from)
                .toList();
    }
}
