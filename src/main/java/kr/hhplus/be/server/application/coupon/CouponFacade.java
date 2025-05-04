package kr.hhplus.be.server.application.coupon;

import kr.hhplus.be.server.domain.coupon.CouponService;
import kr.hhplus.be.server.domain.coupon.UserCoupon;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CouponFacade {
    private final CouponService couponService;
    private final UserService userService;
    //쿠폰 발급
    public void issueCoupon(CouponCommand.Request command) {
        User user = userService.getUserById(command.userId());
        couponService.issueCouponTo(user, command.couponId());
    }

    //보유쿠폰 조회
    public CouponResult getUserCoupons(CouponCommand.Request command) {
        User user = userService.getUserById(command.userId());
        List<UserCoupon> userCoupons = couponService.getUserCoupons(user);
        return CouponResult.from(command.userId(), userCoupons);
    }
}
