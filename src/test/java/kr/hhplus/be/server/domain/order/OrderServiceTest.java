package kr.hhplus.be.server.domain.order;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class OrderServiceTest {

    private OrderRepository orderRepository;
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        orderRepository = mock(OrderRepository.class);
        orderService = new OrderService(orderRepository);
    }

    @Test
    @DisplayName("올바른 주문 저장 성공")
    void createOrder_success() {
        Order order = new Order(1L,
                java.util.List.of(new OrderItem(100L, 1000, 2)),
                null);

        when(orderRepository.save(order)).thenReturn(order);

        Order result = orderService.createOrder(order);

        assertEquals(order, result);
        verify(orderRepository).save(order);
    }

    @Test
    @DisplayName("잘못된 주문 객체 생성 시 예외")
    void createOrder_invalidOrder_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Order(-1L, java.util.List.of(), null);
        });
    }

    @Test
    @DisplayName("주문 수량이 0인 경우 예외")
    void createOrder_invalidQuantity() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Order(1L, java.util.List.of(new OrderItem(1L, 1000, 0)), null);
        });
    }
}
