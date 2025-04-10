package kr.hhplus.be.server.domain.order;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

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
    @DisplayName("정상 주문 저장 시 Repository 호출되고 결과 반환")
    void createOrder_success() {
        // Arrange
        Order order = new Order(1L,
                List.of(new OrderItem(100L, 1000, 1)),
                null,
                null);

        when(orderRepository.save(order)).thenReturn(order);

        // Act
        Order saved = orderService.createOrder(order);

        // Assert
        assertNotNull(saved);
        assertEquals(order.getUserId(), saved.getUserId());
        verify(orderRepository).save(order);
    }
}
