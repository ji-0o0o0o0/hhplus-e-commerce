package kr.hhplus.be.server.domain.coupon;

import kr.hhplus.be.server.domain.coupon.UserCoupon;
import kr.hhplus.be.server.domain.coupon.UserCouponRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserCouponService {

    private final UserCouponRepository userCouponRepository;

    public UserCoupon getValidUserCoupon(Long userId, Long userCouponId) {
        UserCoupon userCoupon = userCouponRepository.findByUserIdAndUserCouponId(userId, userCouponId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자의 쿠폰이 존재하지 않습니다."));

        if (userCoupon.getIsUsed()) {
            throw new IllegalArgumentException("이미 사용된 쿠폰입니다.");
        }

        return userCoupon;
    }

    public void markAsUsed(Long userCouponId, boolean isUsed) {
        userCouponRepository.updateIsUsed(userCouponId, isUsed);
    }
}
