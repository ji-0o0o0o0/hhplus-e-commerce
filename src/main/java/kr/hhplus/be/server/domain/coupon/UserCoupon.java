package kr.hhplus.be.server.domain.coupon;

import kr.hhplus.be.server.common.exception.ApiException;
import kr.hhplus.be.server.domain.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static kr.hhplus.be.server.common.exception.ErrorCode.COUPON_ALREADY_USED;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserCoupon {
    private Long id;
    private Long userId;
    private Long couponId;
    private boolean isUsed;
    private Coupon coupon;

    private UserCoupon(User user, Coupon coupon) {
        this.userId = user.getId();
        this.couponId = coupon.getId();
        this.isUsed = false;
        this.coupon = coupon;
    }

    //생성
    public static UserCoupon create(User user, Coupon coupon) {
        return new UserCoupon(user, coupon);
    }

    //쿠폰 사용여부
    public boolean isAvailable() {
        return !this.isUsed && !this.coupon.isExpired();
    }

    //쿠폰 사용
    public void markAsUsed() {
        if(this.isUsed) throw new ApiException(COUPON_ALREADY_USED);
        this.isUsed = true;
    }

    //쿠폰 복원
    public void rollback() {
        this.isUsed = false;
    }

    public void setCoupon(Coupon coupon) {
        this.coupon = coupon;
    }
}