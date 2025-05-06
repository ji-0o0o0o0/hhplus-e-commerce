package kr.hhplus.be.server.domain.order;

import kr.hhplus.be.server.common.exception.ApiException;
import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.DiscountType;
import kr.hhplus.be.server.domain.coupon.UserCoupon;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.Assert.*;

public class OrderTest {
    @Test
    @DisplayName("정상적인 사용자로 주문 생성성공")
    void createOrder_withValidUser_success(){
        // Arrange
        //유저 생성
        User user = User.create(1L);
        //Act
        //주문 생성
        Order order = Order.create(user);

        //Assert
        //객체 생성 잘 되었는지
        assertNotNull(order);
        assertEquals(order.getUserId(),user.getId());
        assertEquals(order.getStatus(),OrderStatus.NOT_PAID);
    }

    @Test
    @DisplayName("null 사용자로 주문 생성 시 예외 발생")
    void createUser_withNullUser_throwsException(){
        assertThrows(ApiException.class,()-> Order.create(null));
    }

    @Test
    @DisplayName("정상적으로 주문 잘 들어갔는지 확인")
    void addOrderItem_success(){
        //Arrange
        User user = User.create(1L);
        Product product = Product.create("상품1","상품1 설명", BigDecimal.valueOf(1000),2L);
        Order order = Order.create(user);

        //Act
        order.addOrderItem(product,2L);

        //Assert
        //주문 1개
        assertEquals(order.getOrderProducts().size(),1);
        //상품 가격 총 2천원
        assertEquals(BigDecimal.valueOf(2000L),order.getTotalAmount());
    }

    @Test
    @DisplayName("주문 목록에 상품이 없으면 예외 발생")
    void addOrderItem_withNullProduct_ThrowsException(){
        User user = User.create(1L);
        Order order = Order.create(user);

        assertThrows(ApiException.class,()->order.addOrderItem(null,2L));
    }

    @Test
    @DisplayName("재고가 없으면 예외 발생")
    void addOrderItem_withOutOfStockProduct_ThrowsException(){
        User user = User.create(1L);
        Product product = Product.create("상품1","상품1 설명", BigDecimal.valueOf(1000),0L);
        Order order = Order.create(user);

        assertThrows(ApiException.class,()->order.addOrderItem(product,2L));
    }
    @Test
    @DisplayName("수량이 0보다 작으면 예외 발생")
    void addOrderItem_withZeroQuantity_throwsException(){
        User user = User.create(1L);
        Product product = Product.create("상품1","상품1 설명", BigDecimal.valueOf(1000),2L);
        Order order = Order.create(user);

        assertThrows(ApiException.class,()->order.addOrderItem(product,0L));
    }

    @Test
    @DisplayName("쿠폰적용")
    void applyCoupon_success(){
        //Arrange
        User user = User.create(1L);
        Coupon coupon = Coupon.of("10%할인",BigDecimal.valueOf(10L), DiscountType.RATE, LocalDate.now().minusDays(1),LocalDate.now(),2L);
        UserCoupon userCoupon = UserCoupon.create(user,coupon);
        Product product = Product.create("상품1","상품1 설명", BigDecimal.valueOf(1000),2L);
        Order order = Order.create(user);
        order.addOrderItem(product,2L);

        //Act
        order.applyCoupon(userCoupon);

        //Assert
        assertTrue(userCoupon.isUsed());
        assertEquals(new BigDecimal(1800L),order.getTotalAmount());
        assertEquals(order.getUserCouponId(),userCoupon.getCouponId());

    }

    @Test
    @DisplayName("이미 사용한 쿠폰은 예외 발생")
    void applyCoupon_withAlreadyUsed_throwsException(){
        //Arrange
        User user = User.create(1L);
        Coupon coupon = Coupon.of("10%할인",BigDecimal.valueOf(10L), DiscountType.RATE, LocalDate.now().minusDays(1),LocalDate.now(),2L);
        UserCoupon userCoupon = UserCoupon.create(user,coupon);
        userCoupon.markAsUsed();
        Product product = Product.create("상품1","상품1 설명", BigDecimal.valueOf(1000),2L);
        Order order = Order.create(user);
        order.addOrderItem(product,2L);

        assertThrows(ApiException.class,()->order.applyCoupon(userCoupon));
    }

    @Test
    @DisplayName("사용 기한이 지난 쿠폰은 예외 발생")
    void applyCoupon_withExpired_throwsException(){
        //Arrange
        User user = User.create(1L);
        Coupon coupon = Coupon.of("10%할인",BigDecimal.valueOf(10L), DiscountType.RATE, LocalDate.now().minusDays(3),LocalDate.now().minusDays(2),2L);
        UserCoupon userCoupon = UserCoupon.create(user,coupon);
        userCoupon.markAsUsed();
        Product product = Product.create("상품1","상품1 설명", BigDecimal.valueOf(1000),2L);
        Order order = Order.create(user);
        order.addOrderItem(product,2L);

        assertThrows(ApiException.class,()->order.applyCoupon(userCoupon));
    }
    @Test
    @DisplayName("markPaid - NOT_PAID 상태일 경우 결제 상태로 변경")
    void markPaid_fromNotPaid_success() {
        Order order = Order.create(User.create(1L));

        order.markPaid();

        assertEquals(OrderStatus.PAID, order.getStatus());
    }

    @Test
    @DisplayName("markPaid - 이미 결제된 주문은 예외 발생")
    void markPaid_fromPaid_throwsException() {
        Order order = Order.create(User.create(1L));
        order.markPaid();

        assertThrows(ApiException.class, order::markPaid);
    }

    @Test
    @DisplayName("markExpired - NOT_PAID 상태일 경우 만료 처리")
    void markExpired_fromNotPaid_success() {
        Order order = Order.create(User.create(1L));

        order.markExpired();

        assertEquals(OrderStatus.EXPIRED, order.getStatus());
    }

    @Test
    @DisplayName("markExpired - 이미 만료된 주문은 예외 발생")
    void markExpired_fromExpired_throwsException() {
        Order order = Order.create(User.create(1L));
        order.markExpired();

        assertThrows(ApiException.class, order::markExpired);
    }

    @Test
    @DisplayName("markCanceled - NOT_PAID 상태일 경우 취소 처리")
    void markCanceled_fromNotPaid_success() {
        Order order = Order.create(User.create(1L));

        order.markCanceled();

        assertEquals(OrderStatus.CANCEL, order.getStatus());
    }

    @Test
    @DisplayName("markCanceled - 이미 취소된 주문은 예외 발생")
    void markCanceled_fromCanceled_throwsException() {
        Order order = Order.create(User.create(1L));
        order.markCanceled();

        assertThrows(ApiException.class, order::markCanceled);
    }

}
