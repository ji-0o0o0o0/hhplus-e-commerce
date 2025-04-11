package kr.hhplus.be.server.application.order;

import kr.hhplus.be.server.domain.coupon.UserCoupon;
import kr.hhplus.be.server.domain.coupon.UserCouponService;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderItem;
import kr.hhplus.be.server.domain.order.OrderService;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductService;
import kr.hhplus.be.server.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import kr.hhplus.be.server.domain.coupon.CouponService;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OrderFacade implements OrderUseCase {
    //비즈니스 로직을 조합해서 처리하는 핵심 시나리오 담당

    private final ProductService productService;
    private final OrderService orderService;
    private final UserCouponService userCouponService;
    private final CouponService couponService;
    private final UserService userService;

    @Override
    public CreateOrderResult createOrder(CreateOrderCommand command) {
        // 사용자 확인
        userService.getUserById(command.userId());

        // 주문 진행 + 재고확인
        List<OrderItem> orderItems = command.orderItems().stream()
                .map(item -> {
                    Product product = productService.getProductById(item.productId());
                    if (product.getStock() < item.quantity()) {
                        throw new IllegalArgumentException("재고가 부족합니다.");
                    }
                    return new OrderItem(product.getProductId(), product.getPrice(), item.quantity());
                })
                .toList();
        Map<String,Double> couponDiscountInfo = new HashMap<>();
        //쿠폰 유무 확인
        if (command.userCouponId() != null) {
            // 1. 사용자 보유 여부 및 미사용 확인
            UserCoupon userCoupon = userCouponService.getValidUserCoupon(
                    command.userId(), command.userCouponId()
            );
            // 2. 해당 쿠폰 유효한지 (기간 만료 X)
            couponService.getValidCoupon(userCoupon.getCouponId(), LocalDate.now());

            //할인율 적용
            String discountType =userCoupon.getCoupon().getDiscountType();
            double discountValue = userCoupon.getCoupon().getDiscountValue();
            couponDiscountInfo.put(discountType,discountValue);
        }

        //주문생성 후 db 저장
        Order order = new Order(command.userId(), orderItems, command.userCouponId(),couponDiscountInfo);
        Order savedOrder = orderService.createOrder(order);

        //값 반환
        return new CreateOrderResult(savedOrder.getOrderId());
    }
}
