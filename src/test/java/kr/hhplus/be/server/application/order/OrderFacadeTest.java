package kr.hhplus.be.server.application.order;

import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.CouponService;
import kr.hhplus.be.server.domain.coupon.UserCoupon;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderService;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductService;
import kr.hhplus.be.server.domain.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OrderFacadeTest {

    private ProductService productService;
    private OrderService orderService;
    private CouponService couponService;
    private UserCouponService userCouponService;
    private UserService userService;
    private OrderFacade orderFacade;

    @BeforeEach
    void setUp() {
        productService = mock(ProductService.class);
        orderService = mock(OrderService.class);
        userCouponService = mock(UserCouponService.class);
        couponService = mock(CouponService.class);
        userService = mock(UserService.class);

        orderFacade = new OrderFacade(
                productService,
                orderService,
                userCouponService,
                couponService,
                userService
        );
    }

    // return orderId
    private void mockOrderServiceReturnsOrderId(Long orderId) {
        Order mockOrder = mock(Order.class);
        when(mockOrder.getOrderId()).thenReturn(orderId);
        when(orderService.createOrder(any(Order.class))).thenReturn(mockOrder);
    }

    // 성공
    @Test
    @DisplayName("상품 1개 주문 성공(쿠폰없이)")
    void createOrder_success() {
        // Arrange
        long userId = 1L;
        long productId = 100L;
        int quantity = 1;

        // ProductService에서 실제 상품이 조회되도록 mock 세팅
        Product product = new Product(productId, "상품A", 1000, 10);
        when(productService.getProductById(productId)).thenReturn(product);
        // OrderService.save()가 호출시 returnId
        mockOrderServiceReturnsOrderId(999L);

        // Act
        OrderItemCommand item = new OrderItemCommand(productId, quantity);
        OrderCommand command = new OrderCommand(userId, null, List.of(item));

        OrderResult result = orderFacade.createOrder(command);

        // Assert
        assertEquals(Long.valueOf(999L), result.orderId());
        verify(orderService).createOrder(any(Order.class));
    }

    @Test
    @DisplayName("여러 개 상품 주문(쿠폰없이)")
    void createOrder_multipleProducts_success() {
        // Arrange
        long userId = 1L;
        Product p1 = new Product(1L, "A", 1000, 5);
        Product p2 = new Product(2L, "B", 1500, 5);
        when(productService.getProductById(1L)).thenReturn(p1);
        when(productService.getProductById(2L)).thenReturn(p2);
        mockOrderServiceReturnsOrderId(1001L);

        // Act
        OrderCommand command = new OrderCommand(userId, null,
                List.of(new OrderItemCommand(1L, 1), new OrderItemCommand(2L, 2)));

        OrderResult result = orderFacade.createOrder(command);

        // Assert
        assertEquals(Long.valueOf(1001L), result.orderId());
        verify(orderService).createOrder(any(Order.class));
    }

    @Test
    @DisplayName("쿠폰이 RATE 타입일 때 총 금액이 10% 할인되어 적용")
    void createOrder_couponRateDiscount_appliesCorrectly() {
        // Arrange
        long userId = 1L;
        long userCouponId = 10L;
        long couponId = 99L;
        long productId = 100L;
        int quantity = 1;

        // 상품 1000원, 수량 1
        Product product = new Product(productId, "상품A", 1000, 10);
        when(productService.getProductById(productId)).thenReturn(product);
        when(userService.getUserById(userId)).thenReturn(null);

        // 사용자 쿠폰
        Coupon coupon = mock(Coupon.class);
        when(coupon.getDiscountType()).thenReturn("RATE");
        when(coupon.getDiscountValue()).thenReturn(10.0); // 10% 할인

        UserCoupon userCoupon = mock(UserCoupon.class);
        when(userCoupon.getCouponId()).thenReturn(couponId);
        when(userCoupon.getCoupon()).thenReturn(coupon);
        when(userCouponService.getValidUserCoupon(userId, userCouponId)).thenReturn(userCoupon);
        when(couponService.getValidCoupon(eq(couponId), any(LocalDate.class))).thenReturn(coupon);

        // 실제 저장되는 Order 객체 확인
        final Order[] capturedOrder = new Order[1];
        when(orderService.createOrder(any(Order.class))).thenAnswer(invocation -> {
            capturedOrder[0] = invocation.getArgument(0);
            return capturedOrder[0];
        });

        // Act
        OrderItemCommand item = new OrderItemCommand(productId, quantity);
        OrderCommand command = new OrderCommand(userId, userCouponId, List.of(item));
        orderFacade.createOrder(command);

        // Assert
        assertNotNull(capturedOrder[0]);
        assertEquals(900, capturedOrder[0].getTotalAmount()); // 10% 할인 적용 확인
    }
    @Test
    @DisplayName("쿠폰이 AMOUNT 타입일 때 총 금액이 할인되어 적용")
    void createOrder_couponAmountDiscount_appliesCorrectly() {
        // Arrange
        long userId = 1L;
        long userCouponId = 10L;
        long couponId = 99L;
        long productId = 100L;
        int quantity = 1;

        // 상품 10000원, 수량 1
        Product product = new Product(productId, "상품A", 10000, 10);
        when(productService.getProductById(productId)).thenReturn(product);
        when(userService.getUserById(userId)).thenReturn(null);

        // 사용자 쿠폰
        Coupon coupon = mock(Coupon.class);
        when(coupon.getDiscountType()).thenReturn("AMOUNT");
        when(coupon.getDiscountValue()).thenReturn(4000.0); // 4000 할인

        UserCoupon userCoupon = mock(UserCoupon.class);
        when(userCoupon.getCouponId()).thenReturn(couponId);
        when(userCoupon.getCoupon()).thenReturn(coupon);
        when(userCouponService.getValidUserCoupon(userId, userCouponId)).thenReturn(userCoupon);
        when(couponService.getValidCoupon(eq(couponId), any(LocalDate.class))).thenReturn(coupon);

        // 실제 저장되는 Order 객체 확인
        final Order[] capturedOrder = new Order[1];
        when(orderService.createOrder(any(Order.class))).thenAnswer(invocation -> {
            capturedOrder[0] = invocation.getArgument(0);
            return capturedOrder[0];
        });

        // Act
        OrderItemCommand item = new OrderItemCommand(productId, quantity);
        OrderCommand command = new OrderCommand(userId, userCouponId, List.of(item));
        orderFacade.createOrder(command);

        // Assert
        assertNotNull(capturedOrder[0]);
        assertEquals(6000, capturedOrder[0].getTotalAmount()); // 10000-4000=6000 기대
    }

    @Test
    @DisplayName("쿠폰 타입이 AMOUNT, RATE 외의 값이 들어올 시 쿠폰 적용 안됨")
    void createOrder_invalidCouponType_appliesCorrectly() {
        // Arrange
        long userId = 1L;
        long userCouponId = 10L;
        long couponId = 99L;
        long productId = 100L;
        int quantity = 1;

        // 상품 10000원, 수량 1
        Product product = new Product(productId, "상품A", 10000, 10);
        when(productService.getProductById(productId)).thenReturn(product);
        when(userService.getUserById(userId)).thenReturn(null);

        // 사용자 쿠폰
        Coupon coupon = mock(Coupon.class);
        when(coupon.getDiscountType()).thenReturn("NOTHING");
        when(coupon.getDiscountValue()).thenReturn(4000.0); // 4000 할인

        UserCoupon userCoupon = mock(UserCoupon.class);
        when(userCoupon.getCouponId()).thenReturn(couponId);
        when(userCoupon.getCoupon()).thenReturn(coupon);
        when(userCouponService.getValidUserCoupon(userId, userCouponId)).thenReturn(userCoupon);
        when(couponService.getValidCoupon(eq(couponId), any(LocalDate.class))).thenReturn(coupon);

        // 실제 저장되는 Order 객체 확인
        final Order[] capturedOrder = new Order[1];
        when(orderService.createOrder(any(Order.class))).thenAnswer(invocation -> {
            capturedOrder[0] = invocation.getArgument(0);
            return capturedOrder[0];
        });

        // Act
        OrderItemCommand item = new OrderItemCommand(productId, quantity);
        OrderCommand command = new OrderCommand(userId, userCouponId, List.of(item));
        orderFacade.createOrder(command);

        // Assert
        assertNotNull(capturedOrder[0]);
        assertEquals(10000, capturedOrder[0].getTotalAmount()); // 할인 적용 안됨
    }

    // 실패
    @Test
    @DisplayName("재고가 부족하면 예외가 발생")
    void createOrder_stockNotEnough_throwsException() {
        // Arrange
        long userId = 1L;
        long productId = 100L;

        Product product = new Product(productId, "상품A", 1000, 1);
        when(productService.getProductById(productId)).thenReturn(product);

        OrderItemCommand item = new OrderItemCommand(productId, 2);
        OrderCommand command = new OrderCommand(userId, null, List.of(item));

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> orderFacade.createOrder(command));
        verify(orderService, never()).createOrder(any());
    }

    @Test
    @DisplayName("상품 중 하나라도 실패하면 전체 실패")
    void createOrder_partialProductFail_throwsException() {
        // Arrange
        long userId = 1L;

        Product valid = new Product(1L, "상품A", 1000, 5);
        when(productService.getProductById(1L)).thenReturn(valid);
        when(productService.getProductById(2L)).thenThrow(new IllegalArgumentException("상품 없음"));

        // Act + Assert
        OrderCommand command = new OrderCommand(userId, null,
                List.of(new OrderItemCommand(1L, 1), new OrderItemCommand(2L, 1)));

        assertThrows(IllegalArgumentException.class, () -> orderFacade.createOrder(command));
        verify(orderService, never()).createOrder(any());
    }

    @Test
    @DisplayName("존재하지 않는 사용자로 주문 시 예외 발생")
    void createOrder_invalidUser_throwsException() {
        // Arrange
        long userId = 999L;
        long productId = 100L;
        when(userService.getUserById(userId)).thenThrow(new IllegalArgumentException("사용자 없음"));
        OrderItemCommand item = new OrderItemCommand(productId, 1);
        OrderCommand command = new OrderCommand(userId, null, List.of(item));

        // Assert
        assertThrows(IllegalArgumentException.class, () -> orderFacade.createOrder(command));
        verify(orderService, never()).createOrder(any());
    }

    @Test
    @DisplayName("쿠폰 검증이 실패하는 경우(소유권 없음) 예외 발생")
    void createOrder_invalidCouponOwner_throwsException() {
        // Arrange
        long userId = 1L;
        long couponId = 10L;
        long productId = 100L;

        when(productService.getProductById(productId)).thenReturn(new Product(productId, "A", 1000, 10));
        when(userService.getUserById(userId)).thenReturn(null);
        when(userCouponService.getValidUserCoupon(userId, couponId)).thenThrow(new IllegalArgumentException("소유권 없음"));

        OrderCommand command = new OrderCommand(userId, couponId, List.of(new OrderItemCommand(productId, 1)));

        // Assert
        assertThrows(IllegalArgumentException.class, () -> orderFacade.createOrder(command));
        verify(orderService, never()).createOrder(any());
    }

    @Test
    @DisplayName("쿠폰 검증이 실패하는 경우(유효기간 만료) 예외 발생")
    void createOrder_invalidCouponExpired_throwsException() {
        // Arrange
        long userId = 1L;
        long couponId = 10L;
        long couponEntityId = 99L;
        long productId = 100L;

        Product product = new Product(productId, "상품A", 1000, 10);
        when(productService.getProductById(productId)).thenReturn(product);
        when(userService.getUserById(userId)).thenReturn(null);

        UserCoupon userCoupon = mock(UserCoupon.class);
        when(userCoupon.getCouponId()).thenReturn(couponEntityId);
        when(userCouponService.getValidUserCoupon(userId, couponId)).thenReturn(userCoupon);
        when(couponService.getValidCoupon(eq(couponEntityId), any(LocalDate.class)))
                .thenThrow(new IllegalArgumentException("유효하지 않은 쿠폰"));

        OrderCommand command = new OrderCommand(userId, couponId, List.of(new OrderItemCommand(productId, 1)));

        // Assert
        assertThrows(IllegalArgumentException.class, () -> orderFacade.createOrder(command));
        verify(orderService, never()).createOrder(any());
    }
}
