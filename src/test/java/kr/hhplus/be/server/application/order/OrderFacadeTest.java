package kr.hhplus.be.server.application.order;

import kr.hhplus.be.server.domain.order.CouponValidator;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderService;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OrderFacadeTest {

    private ProductService productService;
    private OrderService orderService;
    private CouponValidator  couponValidator;
    private OrderFacade orderFacade;

    @BeforeEach
    void setUp(){
        productService = mock(ProductService.class);
        orderService = mock(OrderService.class);
        couponValidator = mock(CouponValidator.class);

        orderFacade = new OrderFacade(productService,couponValidator,orderService);
    }
    //return orderId
    private void mockOrderServiceReturnsOrderId(Long orderId) {
        Order mockOrder = mock(Order.class);
        when(mockOrder.getOrderId()).thenReturn(orderId);
        when(orderService.createOrder(any(Order.class))).thenReturn(mockOrder);
    }

    //성공
    @Test
    @DisplayName("상품 1개 주문 성공(쿠폰없이)")
    void createOrder_success(){
        //Arrange
        long userId = 1L;
        long productId = 100L;
        int quantity = 1;

        //ProductService에서 실제 상품이 조회되도록 mock 세팅
        Product product = new Product(productId, "상품A", 1000, 10);
        when(productService.getProductById(productId)).thenReturn(product);
        //OrderService.save()가 호출시 returnId
        mockOrderServiceReturnsOrderId(999L);

        //Act
        //주문 요청을 위한 command 객체 생성  및 주문 객체 생성
        OrderItemCommand item = new OrderItemCommand(productId, quantity);
        CreateOrderCommand command = new CreateOrderCommand(userId, null, List.of(item));

        //상품 조회 → 쿠폰 검증 생략 → 주문 저장
        CreateOrderResult result = orderFacade.createOrder(command);


        //Assert
        assertEquals(Long.valueOf(999L), result.orderId());
        verify(orderService).createOrder(any(Order.class));
        verify(couponValidator, never()).validateCouponOwnership(any(), any());
    }

    @Test
    @DisplayName("여러 개 상품 주문(쿠폰없이)")
    void createOrder_multipleProducts_success(){
        //Arrange
        long userId = 1L;

        //여러 개 상품 세팅
        Product p1 = new Product(1L, "A", 1000, 5);
        Product p2 = new Product(2L, "B", 1500, 5);

        when(productService.getProductById(1L)).thenReturn(p1);
        when(productService.getProductById(2L)).thenReturn(p2);
        mockOrderServiceReturnsOrderId(1001L);

        //Act
        //여러 개 주문 생성(쿠폰x)
        CreateOrderCommand command = new CreateOrderCommand(userId, null,
                List.of(new OrderItemCommand(1L, 1), new OrderItemCommand(2L, 2)));

        CreateOrderResult result = orderFacade.createOrder(command);
        //Assert
        assertEquals(Long.valueOf(1001L), result.orderId());
        verify(orderService).createOrder(any(Order.class));
        verify(couponValidator, never()).validateCouponOwnership(any(), any());
    }
    @Test
    @DisplayName("쿠폰이 있는 경우 주문 성공")
    void createOrder_withCoupon_success(){
        //Arrange
        long userId = 1L;
        long couponId = 10L;
        long productId = 100L;

        //상품 세팅
        Product product = new Product(productId, "상품A", 1000, 10);
        when(productService.getProductById(productId)).thenReturn(product);
        mockOrderServiceReturnsOrderId(2000L);

        //Act
        //주문 세팅
        OrderItemCommand item = new OrderItemCommand(productId, 1);
        CreateOrderCommand command = new CreateOrderCommand(userId, couponId, List.of(item));
        CreateOrderResult result = orderFacade.createOrder(command);

        //Assert
        assertEquals(Long.valueOf(2000L), result.orderId());
        verify(orderService).createOrder(any(Order.class));
        verify(couponValidator).validateCouponOwnership(userId, couponId);
    }

    //실패
    @Test
    @DisplayName(" 재고가 부족하면 예외가 발생")
    void createOrder_stockNotEnough_throwsException(){
        //Arrange
        long userId = 1L;
        long productId = 100L;

        // product 생성은 정상 범위로 (나중에 stock 검증 내부에서 하게)
        Product product = new Product(productId, "상품A", 1000, 1);
        when(productService.getProductById(productId)).thenReturn(product);

        //Act+Assert
        // 주문은 실제 재고보다 많은 수량으로 요청
        OrderItemCommand item = new OrderItemCommand(productId, 2);
        CreateOrderCommand command = new CreateOrderCommand(userId, null, List.of(item));

        assertThrows(IllegalArgumentException.class, () -> orderFacade.createOrder(command));

        //주문이 들어가면 안됨
        verify(orderService, never()).createOrder(any());
    }
   /* @Test
    @DisplayName("쿠폰 검증이 실패하는 경우(소유권 없음)")
    void createOrder_invalidCouponOwner_throwsException(){
        //Arrange
        //Act+Assert
    }
    @Test
    @DisplayName("쿠폰 검증이 실패하는 경우(쿠폰이 유효하지 않음)")
    void createOrder_invalidCoupon_throwsException(){
        //Arrange

        //Act+Assert
    }*/
    @Test
    @DisplayName("상품 중 하나라도 실패하면 전체 실패")
    void createOrder_partialProductFail_throwsException	(){
        //Arrange
        long userId = 1L;

        Product valid = new Product(1L, "상품A", 1000, 5);
        when(productService.getProductById(1L)).thenReturn(valid);
        when(productService.getProductById(2L)).thenThrow(new IllegalArgumentException("상품 없음"));
        //Act+Assert
        CreateOrderCommand command = new CreateOrderCommand(userId, null,
                List.of(new OrderItemCommand(1L, 1), new OrderItemCommand(2L, 1)));

        assertThrows(IllegalArgumentException.class, () -> orderFacade.createOrder(command));
        verify(orderService, never()).createOrder(any());
    }
}