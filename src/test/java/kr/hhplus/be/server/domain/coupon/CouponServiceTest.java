package kr.hhplus.be.server.domain.coupon;

import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.CouponRepository;
import kr.hhplus.be.server.domain.coupon.CouponService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CouponServiceTest {

    private CouponRepository couponRepository;
    private CouponService couponService;

    @BeforeEach
    void setUp() {
        couponRepository = mock(CouponRepository.class);
        couponService = new CouponService(couponRepository);
    }

    @Test
    @DisplayName("유효한 쿠폰 조회 성공")
    void getValidCoupon_success() {
        // Arrange
        Long couponId = 1L;
        LocalDate today = LocalDate.now();
        Coupon coupon = new Coupon(couponId, "할인쿠폰", 10, "RATE", today.minusDays(1), today.plusDays(1), 10);
        when(couponRepository.findByCouponId(couponId)).thenReturn(Optional.of(coupon));

        // Act
        Coupon result = couponService.getValidCoupon(couponId, today);

        // Assert
        assertEquals(couponId, result.getId());
    }

    @Test
    @DisplayName("쿠폰이 존재하지 않으면 예외")
    void getValidCoupon_notFound_throws() {
        // Arrange
        Long couponId = 999L;
        LocalDate today = LocalDate.now();
        when(couponRepository.findByCouponId(couponId)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> couponService.getValidCoupon(couponId, today));
        assertEquals("쿠폰이 존재하지 않습니다.", ex.getMessage());
    }

    @Test
    @DisplayName("쿠폰이 만료된 경우 예외")
    void getValidCoupon_expired_throws() {
        // Arrange
        Long couponId = 1L;
        LocalDate today = LocalDate.now();
        Coupon expiredCoupon = new Coupon(couponId, "만료쿠폰", 10, "RATE", today.minusDays(10), today.minusDays(1), 10);
        when(couponRepository.findByCouponId(couponId)).thenReturn(Optional.of(expiredCoupon));

        // Act & Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> couponService.getValidCoupon(couponId, today));
        assertEquals("쿠폰 유효기간이 지났습니다.", ex.getMessage());
    }
}
