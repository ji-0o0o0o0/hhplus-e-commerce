package kr.hhplus.be.server.domain.coupon;

import kr.hhplus.be.server.domain.coupon.UserCoupon;
import kr.hhplus.be.server.domain.coupon.UserCouponRepository;

public class UserCouponService {

    private final UserCouponRepository userCouponRepository;

    public UserCouponService(UserCouponRepository userCouponRepository) {
        this.userCouponRepository = userCouponRepository;
    }

    public UserCoupon getValidUserCoupon(Long userId, Long userCouponId) {
        UserCoupon userCoupon = userCouponRepository.findByUserCouponId(userCouponId)
                .orElseThrow(() -> new IllegalArgumentException("해당 쿠폰이 존재하지 않습니다."));

        if (!userCoupon.getUserId().equals(userId)) {
            throw new IllegalArgumentException("해당 사용자의 쿠폰이 아닙니다.");
        }

        if (userCoupon.getIsUsed()) {
            throw new IllegalArgumentException("이미 사용된 쿠폰입니다.");
        }

        return userCoupon;
    }
}
