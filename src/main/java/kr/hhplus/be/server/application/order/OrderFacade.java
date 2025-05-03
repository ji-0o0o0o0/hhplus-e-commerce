package kr.hhplus.be.server.application.order;

import kr.hhplus.be.server.domain.coupon.UserCoupon;
import kr.hhplus.be.server.domain.coupon.UserCouponService;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderItem;
import kr.hhplus.be.server.domain.order.OrderService;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductService;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import kr.hhplus.be.server.domain.coupon.CouponService;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class OrderFacade {
    //비즈니스 로직을 조합해서 처리하는 핵심 시나리오 담당

    private final ProductService productService;
    private final OrderService orderService;
    private final UserCouponService userCouponService;
    private final CouponService couponService;
    private final UserService userService;

   public OrderResult order(OrderCommand orderCommand){
       //1. 사용자 정보 확인
       User user = userService.getUserById(orderCommand.userId());
       //2. 주문 도메인 객체 생성
       Order order = Order.create(user);
       //3. 주문 상품 추가 및 재고 차감
       orderCommand.orderItems().forEach(item->{
           Product product = productService.getProductById(item.productId());
           product.decreaseStock(item.quantity());
           order.addOrderItem(product, item.quantity());
       });
       //4. 쿠폰 적용(선택)
       if(orderCommand.userCouponId()!=null){
           //사용자가 쿠폰 있는지 확인
           UserCoupon userCoupon = userCouponService.getValidUserCoupon(orderCommand.userId(),orderCommand.userCouponId());
           //쿠폰 유효성 확인
           couponService.getValidCoupon(orderCommand.userCouponId(),LocalDate.now());
           //쿠폰 적용
           order.applyCoupon(userCoupon);
       }
       //5. 주문 저장 및 반환
       Order savedOrder = orderService.order(order);
       return OrderResult.from(savedOrder);
   }
}
