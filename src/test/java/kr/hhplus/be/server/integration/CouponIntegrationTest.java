package kr.hhplus.be.server.integration;

import kr.hhplus.be.server.application.coupon.CouponCommand;
import kr.hhplus.be.server.application.coupon.CouponFacade;
import kr.hhplus.be.server.application.coupon.CouponResult;
import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.CouponRepository;
import kr.hhplus.be.server.domain.coupon.DiscountType;
import kr.hhplus.be.server.domain.point.Point;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserRepository;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;
import static org.instancio.Select.field;

@SpringBootTest
@Transactional
@Rollback
@ActiveProfiles("test")
class CouponIntegrationTest {

    @Autowired
    private CouponFacade couponFacade;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CouponRepository couponRepository;

    private User user;
    private Long couponId;

    private Coupon createValidCoupon(String name, long stock) {
        return new Coupon(
                name,
                new BigDecimal("1000"),
                DiscountType.AMOUNT,
                LocalDate.now().minusDays(1),
                LocalDate.now().plusDays(3),
                stock
        );
    }

    private Coupon createExpiredCoupon(String name, long stock) {
        return new Coupon(
                name,
                new BigDecimal("1000"),
                DiscountType.AMOUNT,
                LocalDate.now().minusDays(5),
                LocalDate.now().minusDays(1),
                stock
        );
    }

    @BeforeEach
    void setup() {
        user = userRepository.save(Instancio.of(User.class)
                .supply(field(User::getId), () -> null)
                .create());
        //pointRepository.savePoint(Point.of(user.getId(), BigDecimal.ZERO));

        var coupon = createValidCoupon("테스트쿠폰", 10L);
        couponRepository.save(coupon);
        this.couponId = coupon.getId();
    }

    @Test
    @DisplayName("정상 쿠폰 발급 성공")
    void testIssueCouponSuccess() {
        // Arrange
        var command = new CouponCommand.IssueRequest(user.getId(), couponId);

        // Act
        CouponResult.Issue result = couponFacade.issueCoupon(command);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.userId()).isEqualTo(user.getId());
        assertThat(result.couponId()).isEqualTo(couponId);
    }

    @Test
    @DisplayName("중복 발급 방지")
    void testDuplicateIssueFails() {
        // Arrange
        var command = new CouponCommand.IssueRequest(user.getId(), couponId);
        couponFacade.issueCoupon(command);

        // Act & Assert
        assertThatThrownBy(() -> couponFacade.issueCoupon(command))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("쿠폰 잔여 수량 없음 예외")
    void testIssueCouponOutOfStock() {
        // Arrange
        var coupon = createValidCoupon("품절쿠폰", 0L);
        couponRepository.save(coupon);

        var command = new CouponCommand.IssueRequest(user.getId(), coupon.getId());

        // Act & Assert
        assertThatThrownBy(() -> couponFacade.issueCoupon(command))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("만료된 쿠폰 발급 실패")
    void testIssueExpiredCouponFails() {
        // Arrange
        var expired = createExpiredCoupon("만료쿠폰", 5L);
        couponRepository.save(expired);

        var command = new CouponCommand.IssueRequest(user.getId(), expired.getId());

        // Act & Assert
        assertThatThrownBy(() -> couponFacade.issueCoupon(command))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("보유 쿠폰 조회 성공")
    void testUserCouponsQuerySuccess() {
        // Arrange
        CouponCommand.IssueRequest couponRequest = CouponCommand.IssueRequest.from(user.getId(), couponId);
        couponFacade.issueCoupon(couponRequest);

        // Act
        CouponResult.UserCoupons result = couponFacade.getUserCoupons(couponRequest);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.userId()).isEqualTo(user.getId());
        assertThat(result.userCoupons()).hasSize(1);
        assertThat(result.userCoupons().get(0).getCouponId()).isEqualTo(couponId);
    }

    @Test
    @DisplayName("보유 쿠폰 조회 - 보유한 쿠폰 없음")
    void testUserCouponsEmptyResult() {
        // Act
        CouponCommand.IssueRequest couponRequest = CouponCommand.IssueRequest.from(user.getId(), couponId);
        CouponResult.UserCoupons result = couponFacade.getUserCoupons(couponRequest);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.userId()).isEqualTo(user.getId());
        assertThat(result.userCoupons()).isEmpty();
    }
}
