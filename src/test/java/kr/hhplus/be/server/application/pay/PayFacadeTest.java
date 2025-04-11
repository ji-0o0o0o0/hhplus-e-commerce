package kr.hhplus.be.server.application.pay;

import kr.hhplus.be.server.application.pay.PayCommand;
import kr.hhplus.be.server.application.pay.PayFacade;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderService;
import kr.hhplus.be.server.domain.order.OrderStatus;
import kr.hhplus.be.server.domain.pay.PayService;
import kr.hhplus.be.server.domain.point.Point;
import kr.hhplus.be.server.domain.point.PointService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PayFacadeTest {

    private OrderService orderService;
    private PointService pointService;
    private PayService payService;
    private PayFacade payFacade;

    @BeforeEach
    void setUp() {
        orderService = mock(OrderService.class);
        pointService = mock(PointService.class);
        payService = mock(PayService.class);

        payFacade = new PayFacade(orderService, pointService, payService);
    }

    @Test
    @DisplayName("결제 성공")
    void pay_success() {
        Long orderId = 1L;
        Long userId = 10L;
        int amount = 100_000;
        Order order = mock(Order.class);

        when(order.getUserId()).thenReturn(userId);
        when(order.getTotalAmount()).thenReturn(amount);
        when(order.getOrderStatus()).thenReturn(OrderStatus.NOT_PAID);
        when(orderService.getOrder(orderId)).thenReturn(order);

        Point point = new Point(1L,  200_000);
        when(pointService.getOrCreatePoint(userId)).thenReturn(point);

        payFacade.pay(new PayCommand(orderId));

        verify(pointService).use(userId, amount);
        verify(orderService).markPaid(orderId);
        verify(payService).sendOrderData(order);
    }


    @Test
    @DisplayName("결제 실패 - 잔액 부족")
    void pay_fail_insufficientBalance() {
        Long orderId = 2L;
        Long userId = 20L;
        int amount = 100_000;
        Order order = mock(Order.class);

        when(order.getUserId()).thenReturn(userId);
        when(order.getTotalAmount()).thenReturn(amount);
        when(order.getOrderStatus()).thenReturn(OrderStatus.NOT_PAID);
        when(orderService.getOrder(orderId)).thenReturn(order);

        // 포인트가 부족한 상태로 설정
        Point point = new Point(2L, 50_000);
        when(pointService.getOrCreatePoint(userId)).thenReturn(point);

        PayCommand command = new PayCommand(orderId);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> payFacade.pay(command));
        assertEquals("포인트 잔액이 부족합니다.", ex.getMessage());
    }

    @Test
    @DisplayName("결제 실패 - 주문 상태가 NOT_PAID 아님")
    void pay_fail_invalidOrderStatus() {
        Long orderId = 3L;
        Long userId = 30L;
        int amount = 100_000;
        Order order = mock(Order.class);

        when(order.getUserId()).thenReturn(userId);
        when(order.getTotalAmount()).thenReturn(amount);
        when(order.getOrderStatus()).thenReturn(OrderStatus.CANCEL);
        when(orderService.getOrder(orderId)).thenReturn(order);

        PayCommand command = new PayCommand(orderId);

        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> payFacade.pay(command));
        assertEquals("주문 상태가 결제 대기 상태가 아닙니다.", ex.getMessage());
    }
}