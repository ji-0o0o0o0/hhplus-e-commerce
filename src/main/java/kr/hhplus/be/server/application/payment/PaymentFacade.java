package kr.hhplus.be.server.application.payment;

import kr.hhplus.be.server.application.order.OrderResult;
import kr.hhplus.be.server.common.exception.ApiException;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderService;
import kr.hhplus.be.server.domain.order.OrderStatus;
import kr.hhplus.be.server.domain.paymant.PaymentService;
import kr.hhplus.be.server.domain.point.Point;
import kr.hhplus.be.server.domain.point.PointService;
import kr.hhplus.be.server.domain.point.PointUseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import static kr.hhplus.be.server.common.exception.ErrorCode.INVALID_ORDER_USER;

@RequiredArgsConstructor
@Component
public class PaymentFacade {

    private final OrderService orderService;
    private final PointService pointService;

    public PaymentResult pay(PaymentCommand command) {
        // 1. 주문 조회
        Order order = orderService.getOrderById(command.orderId());
        if(!order.getUserId().equals(command.userId())) throw new ApiException(INVALID_ORDER_USER);

        //2. 포인트 사용
        pointService.decrease(command.userId(), order.getTotalAmount());

        //3. 주문 완료(상태 변경 및 외부 플랫폼 전송)
        orderService.completeOrder(order);

        return PaymentResult.from(order);
    }
}
