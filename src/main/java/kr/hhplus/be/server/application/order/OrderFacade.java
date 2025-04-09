package kr.hhplus.be.server.application.order;

import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderItem;
import kr.hhplus.be.server.domain.order.OrderService;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductService;
import kr.hhplus.be.server.domain.order.CouponValidator;
import lombok.RequiredArgsConstructor;

import java.util.List;


@RequiredArgsConstructor
public class OrderFacade implements OrderUseCase {
    //비즈니스 로직을 조합해서 처리하는 핵심 시나리오 담당

    private final ProductService productService;
    private final CouponValidator couponValidator;
    private final OrderService orderService;

    @Override
    public CreateOrderResult createOrder(CreateOrderCommand command) {
        // 주문 진행
        List<OrderItem> orderItems = command.orderItems().stream()
                .map(item -> {
                    Product product = productService.getProductById(item.productId()); // ✅ 서비스 호출
                    return new OrderItem(product.getProductId(), product.getPrice(), item.quantity());
                })
                .toList();
        //쿠폰 유무 확인
        if (command.userCouponId() != null) {
            couponValidator.validateCouponOwnership(command.userId(), command.userCouponId());
        }

        //주문생성 후 db 저장
        Order order = new Order(command.userId(), orderItems, command.userCouponId());
        orderService.save(order);

        //값 반환
        return new CreateOrderResult(order.getUserId());
    }
}
