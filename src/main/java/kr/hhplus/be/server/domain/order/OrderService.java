package kr.hhplus.be.server.domain.order;


import kr.hhplus.be.server.domain.coupon.UserCouponService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserCouponService userCouponService;

    public Order createOrder(Order order) {
        if (order.getUserCouponId() != null) {
            //쿠폰 사용 업데이트
            userCouponService.markAsUsed(order.getUserCouponId(),true);
        }
        return orderRepository.save(order);
    }
}