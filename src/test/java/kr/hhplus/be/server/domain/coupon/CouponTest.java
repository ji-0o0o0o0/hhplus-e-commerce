package kr.hhplus.be.server.domain.coupon;

import kr.hhplus.be.server.common.exception.ApiException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.Assert.*;

@DisplayName("Coupon 도메인 단위 테스트")
class CouponTest {

    @Test
    @DisplayName("정률 할인 쿠폰 적용 - 총 금액의 10% 할인")
    void getDiscountAmount_percentCoupon_returnsCorrectDiscount() {
        Coupon coupon = Coupon.of("10% 할인", BigDecimal.valueOf(10), DiscountType.RATE,
                LocalDate.now().minusDays(1), LocalDate.now().plusDays(1), 10L);

        BigDecimal discount = coupon.getDiscountAmount(BigDecimal.valueOf(10000));

        assertEquals(BigDecimal.valueOf(1000), discount);
    }

    @Test
    @DisplayName("정액 할인 쿠폰 적용 - 2000원 할인 (최대 총 금액까지)")
    void getDiscountAmount_amountCoupon_returnsCorrectDiscount() {
        Coupon coupon = Coupon.of("2000원 할인", BigDecimal.valueOf(2000), DiscountType.AMOUNT,
                LocalDate.now().minusDays(1), LocalDate.now().plusDays(1), 10L);

        BigDecimal discount = coupon.getDiscountAmount(BigDecimal.valueOf(10000));

        assertEquals(BigDecimal.valueOf(2000), discount);
    }

    @Test
    @DisplayName("정액 할인 쿠폰이 총 금액보다 클 경우, 총 금액 만큼만 할인")
    void getDiscountAmount_exceedsTotalAmount_capped() {
        Coupon coupon = Coupon.of("2000원 할인", BigDecimal.valueOf(2000), DiscountType.AMOUNT,
                LocalDate.now().minusDays(1), LocalDate.now().plusDays(1), 10L);

        BigDecimal discount = coupon.getDiscountAmount(BigDecimal.valueOf(1500));

        assertEquals(BigDecimal.valueOf(1500), discount);
    }

    @Test
    @DisplayName("쿠폰 발급 시 재고 감소")
    void issuance_decrementsStock() {
        Coupon coupon = Coupon.of("5%", BigDecimal.valueOf(5), DiscountType.RATE,
                LocalDate.now().minusDays(1), LocalDate.now().plusDays(1), 1L);

        coupon.issuance();

        assertEquals(Optional.of(0L), coupon.getStock());
    }

    @Test
    @DisplayName("쿠폰 재고가 0이면 발급 시 예외 발생")
    void issuance_whenStockZero_throwsException() {
        Coupon coupon = Coupon.of("5%", BigDecimal.valueOf(5), DiscountType.RATE,
                LocalDate.now().minusDays(1), LocalDate.now().plusDays(1), 0L);

        assertThrows(ApiException.class, coupon::issuance);
    }

    @Test
    @DisplayName("쿠폰이 유효기간 내면 만료되지 않음")
    void isExpired_withinValidPeriod_returnsFalse() {
        Coupon coupon = Coupon.of("Valid", BigDecimal.ONE, DiscountType.AMOUNT,
                LocalDate.now().minusDays(1), LocalDate.now().plusDays(1), 5L);

        assertFalse(coupon.isExpired());
    }

    @Test
    @DisplayName("쿠폰이 유효기간을 벗어나면 만료됨")
    void isExpired_afterValidPeriod_returnsTrue() {
        Coupon coupon = Coupon.of("Expired", BigDecimal.ONE, DiscountType.AMOUNT,
                LocalDate.now().minusDays(10), LocalDate.now().minusDays(1), 5L);

        assertTrue(coupon.isExpired());
    }
}
