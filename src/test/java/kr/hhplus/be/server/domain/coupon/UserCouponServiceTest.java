package kr.hhplus.be.server.domain.coupon;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserCouponServiceTest {

    private UserCouponRepository repository;
    private UserCouponService service;

    @BeforeEach
    void setUp() {
        repository = mock(UserCouponRepository.class);
        service = new UserCouponService(repository);
    }

    @Test
    @DisplayName("유효한 사용자 쿠폰 조회 성공")
    void getValidUserCoupon_success() {
        // Arrange
        Long userId = 1L;
        Long userCouponId = 100L;
        UserCoupon coupon = new UserCoupon(userCouponId, userId, 10L, LocalDateTime.now(), false, null);
        when(repository.findByUserIdAndUserCouponId(userId, userCouponId)).thenReturn(Optional.of(coupon));
        //Act
        UserCoupon result = service.getValidUserCoupon(userId, userCouponId);
        //Assert
        assertEquals(userId, result.getUserId());
        assertFalse(result.getIsUsed());
    }

    @Test
    @DisplayName("쿠폰 존재하지 않으면 예외")
    void getValidUserCoupon_notFound_throws() {
        // Arrange
        Long userId = 1L;
        Long userCouponId = 100L;
        when(repository.findByUserIdAndUserCouponId(userId, userCouponId)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.getValidUserCoupon(userId, userCouponId));
        assertEquals("해당 사용자의 쿠폰이 존재하지 않습니다.", ex.getMessage());
    }

    @Test
    @DisplayName("사용된 쿠폰이면 예외")
    void getValidUserCoupon_used_throws() {
        // Arrange 사용한 쿠폰 세팅
        Long userId = 1L;
        Long userCouponId = 100L;
        UserCoupon coupon = new UserCoupon(userCouponId, userId, 10L, LocalDateTime.now(), true, null);
        when(repository.findByUserIdAndUserCouponId(userId, userCouponId)).thenReturn(Optional.of(coupon));
        //Act+Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.getValidUserCoupon(userId, userCouponId));
        assertEquals("이미 사용된 쿠폰입니다.", ex.getMessage());
    }

    @Test
    @DisplayName("markAsUsed 호출 시 true 전달")
    void markAsUsed_true() {
        // Arrange
        Long userCouponId = 100L;
        //Act
        service.markAsUsed(userCouponId, true);
        //Assert
        verify(repository).updateIsUsed(userCouponId, true);
    }

    @Test
    @DisplayName("markAsUsed 호출 시 false 전달")
    void markAsUsed_false() {
        // Arrange
        Long userCouponId = 200L;
        //Act
        service.markAsUsed(userCouponId, false);
        //Assert
        verify(repository).updateIsUsed(userCouponId, false);
    }
}