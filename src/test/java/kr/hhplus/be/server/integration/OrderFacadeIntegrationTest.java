package kr.hhplus.be.server.integration;

import kr.hhplus.be.server.application.order.OrderCommand;
import kr.hhplus.be.server.application.order.OrderFacade;
import kr.hhplus.be.server.application.order.OrderItemCommand;
import kr.hhplus.be.server.application.order.OrderResult;
import kr.hhplus.be.server.domain.coupon.*;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductRepository;
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
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.instancio.Select.field;

@SpringBootTest
@Transactional
@Rollback
@ActiveProfiles("test")
class OrderFacadeIntegrationTest {

    @Autowired
    private OrderFacade orderFacade;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private UserCouponRepository userCouponRepository;

    private User user;
    private Product product;
    private UserCoupon userCoupon;

    @BeforeEach
    void setup() {
        user = userRepository.save(Instancio.of(User.class)
                .supply(field(User::getId), () -> null)
                .create());

        product =Product.create("테스트상품", "테스트설명", new BigDecimal("10000"), 100L);
        productRepository.save(product);


        Coupon coupon = Coupon.of("1천원할인", new BigDecimal("1000"), DiscountType.AMOUNT,
                LocalDate.now().minusDays(1), LocalDate.now().plusDays(2), 10L);
        couponRepository.save(coupon);

        userCoupon = UserCoupon.create(user,coupon);
        userCouponRepository.save(userCoupon);
    }

    @Test
    @DisplayName("상품 주문 생성 성공")
    void testCreateOrderSuccess() {
        // Arrange
        OrderCommand command = new OrderCommand(
                user.getId(),
                null,
                List.of(new OrderItemCommand(product.getId(), 2L))
        );

        // Act
        OrderResult result = orderFacade.order(command);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.orderId()).isNotNull();
    }

    @Test
    @DisplayName("재고 초과 주문 예외")
    void testCreateOrderFailInsufficientStock() {
        // Arrange
        OrderCommand command = new OrderCommand(
                user.getId(),
                null,
                List.of(new OrderItemCommand(product.getId(), 1000L))
        );

        // Act & Assert
        assertThatThrownBy(() -> orderFacade.order(command))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("쿠폰이 적용된 주문 생성 성공")
    void testCreateOrderWithCouponSuccess() {
        // Arrange
        OrderCommand command = new OrderCommand(
                user.getId(),
                userCoupon.getCouponId(),
                List.of(new OrderItemCommand(product.getId(), 1L))
        );

        // Act
        OrderResult result = orderFacade.order(command);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.orderId()).isNotNull();
    }
}
