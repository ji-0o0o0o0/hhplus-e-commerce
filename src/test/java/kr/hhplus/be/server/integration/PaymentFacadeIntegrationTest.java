package kr.hhplus.be.server.integration;

import kr.hhplus.be.server.application.order.OrderCommand;
import kr.hhplus.be.server.application.order.OrderFacade;
import kr.hhplus.be.server.application.order.OrderItemCommand;
import kr.hhplus.be.server.application.order.OrderResult;
import kr.hhplus.be.server.application.payment.PaymentCommand;
import kr.hhplus.be.server.application.payment.PaymentFacade;
import kr.hhplus.be.server.domain.order.OrderStatus;
import kr.hhplus.be.server.domain.point.Point;
import kr.hhplus.be.server.domain.point.PointRepository;
import kr.hhplus.be.server.domain.point.PointUseStatus;
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
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.instancio.Select.field;

@SpringBootTest
@Transactional
@Rollback
@ActiveProfiles("test")
class PaymentFacadeIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PointRepository pointRepository;

    @Autowired
    private OrderFacade orderFacade;

    @Autowired
    private PaymentFacade paymentFacade;

    private User user;
    private Product product;

    @BeforeEach
    void setup() {
        user = userRepository.save(Instancio.of(User.class)
                .supply(field(User::getId), () -> null)
                .create());
        pointRepository.savePoint(Point.of(user.getId(), new BigDecimal("20000")));
        product = Product.create("상품A", "설명", new BigDecimal("10000"), 100L);
        productRepository.save(product);
    }

    @Test
    @DisplayName("결제 성공 - 포인트 차감 및 주문 상태 변경")
    void testPaymentSuccess() {
        // Arrange
        OrderCommand orderCommand = new OrderCommand(
                user.getId(),
                null,
                List.of(new OrderItemCommand(product.getId(), 2L))
        );
        OrderResult orderResult = orderFacade.order(orderCommand);

        PaymentCommand paymentCommand = PaymentCommand.from(user.getId(), orderResult.orderId());

        // Act
        var result = paymentFacade.pay(paymentCommand);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.orderId()).isEqualTo(orderResult.orderId());
        assertThat(result.orderStatus()).isEqualTo(OrderStatus.PAID);

        var pointHistories = pointRepository.findPointHistoryByUserId(user.getId());
        assertThat(pointHistories).hasSize(1);
        assertThat(pointHistories.get(0).getAmount()).isEqualByComparingTo("20000");
        assertThat(pointHistories.get(0).getType()).isEqualTo(PointUseStatus.USE);
    }

    @Test
    @DisplayName("결제 실패 - 잔액 부족")
    void testPaymentFailInsufficientBalance() {
        // Arrange
        pointRepository.savePoint(Point.of(user.getId(), new BigDecimal("5000")));

        OrderCommand orderCommand = new OrderCommand(
                user.getId(),
                null,
                List.of(new OrderItemCommand(product.getId(), 1L))
        );
        OrderResult orderResult = orderFacade.order(orderCommand);
        PaymentCommand paymentCommand = PaymentCommand.from(user.getId(), orderResult.orderId());

        // Act & Assert
        assertThatThrownBy(() -> paymentFacade.pay(paymentCommand))
                .isInstanceOf(RuntimeException.class);

        var pointHistories = pointRepository.findPointHistoryByUserId(user.getId());
        assertThat(pointHistories).isEmpty();
    }
}
