package kr.hhplus.be.server.domain.coupon;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class UserCoupon {
    private final Long id;
    private final Long userId;
    private final Long couponId;
    private final LocalDateTime updatedAt;
    private boolean isUsed;
    private Coupon coupon;

    public UserCoupon(Long id, Long userId, Long couponId, LocalDateTime updatedAt,
                      boolean isUsed,Coupon coupon) {
        this.id = id;
        this.userId = userId;
        this.couponId = couponId;
        this.updatedAt = updatedAt;
        this.isUsed = isUsed;
        this.coupon=coupon;
    }
    //사용여부
    public void markAsUsed(boolean isUsed) {
        this.isUsed = !isUsed;
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getCouponId() {
        return couponId;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public boolean getIsUsed() {
        return isUsed;
    }

    public Coupon getCoupon() {
        return coupon;
    }
}