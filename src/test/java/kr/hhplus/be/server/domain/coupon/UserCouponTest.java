package kr.hhplus.be.server.domain.coupon;

import kr.hhplus.be.server.common.exception.ApiException;
import kr.hhplus.be.server.domain.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.Assert.*;

@DisplayName("UserCoupon 도메인 단위 테스트")
class UserCouponTest {

    @Test
    @DisplayName("사용하지 않았고, 유효기간 내 쿠폰은 사용 가능")
    void isAvailable_whenValidAndNotUsed_returnsTrue() {
        User user = User.create(1L);
        Coupon coupon = Coupon.of("10%", BigDecimal.TEN, DiscountType.RATE,
                LocalDate.now().minusDays(1), LocalDate.now().plusDays(1), 5L);

        UserCoupon userCoupon = UserCoupon.create(user, coupon);

        assertTrue(userCoupon.isAvailable());
    }

    @Test
    @DisplayName("이미 사용한 쿠폰은 사용 불가")
    void isAvailable_whenUsed_returnsFalse() {
        User user = User.create(1L);
        Coupon coupon = Coupon.of("10%", BigDecimal.TEN, DiscountType.RATE,
                LocalDate.now().minusDays(1), LocalDate.now().plusDays(1), 5L);

        UserCoupon userCoupon = UserCoupon.create(user, coupon);
        userCoupon.markAsUsed();

        assertThrows(ApiException.class,userCoupon::isAvailable);
    }

    @Test
    @DisplayName("유효기간이 지난 쿠폰은 사용 불가")
    void isAvailable_whenExpired_returnsFalse() {
        User user = User.create(1L);
        Coupon coupon = Coupon.of("10%", BigDecimal.TEN, DiscountType.RATE,
                LocalDate.now().minusDays(10), LocalDate.now().minusDays(1), 5L);

        UserCoupon userCoupon = UserCoupon.create(user, coupon);

        assertThrows(ApiException.class,userCoupon::isAvailable);
    }

    @Test
    @DisplayName("markAsUsed 호출 시 쿠폰은 사용 처리됨")
    void markAsUsed_setsIsUsedTrue() {
        User user = User.create(1L);
        Coupon coupon = Coupon.of("10%", BigDecimal.TEN, DiscountType.RATE,
                LocalDate.now().minusDays(1), LocalDate.now().plusDays(1), 5L);

        UserCoupon userCoupon = UserCoupon.create(user, coupon);

        userCoupon.markAsUsed();

        assertTrue(userCoupon.isUsed());
    }
}
