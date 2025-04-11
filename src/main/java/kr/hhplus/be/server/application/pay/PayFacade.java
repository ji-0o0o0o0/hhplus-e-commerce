package kr.hhplus.be.server.application.pay;

import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderService;
import kr.hhplus.be.server.domain.order.OrderStatus;
import kr.hhplus.be.server.domain.pay.PayService;
import kr.hhplus.be.server.domain.pay.PayRepository;
import kr.hhplus.be.server.domain.point.Point;
import kr.hhplus.be.server.domain.point.PointService;
import kr.hhplus.be.server.domain.point.PointHistory;
import kr.hhplus.be.server.domain.point.PointUseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PayFacade {

    private final OrderService orderService;
    private final PointService pointService;
    private final PayService payService;


    public void pay(PayCommand command) {
        Long orderId = command.orderId();

        // 1. 주문 조회
        Order order = orderService.getOrder(orderId);
        Long userId = order.getUserId();
        int totalAmount = order.getTotalAmount();
        OrderStatus status = order.getOrderStatus();

        // 2. 주문 상태 정책 검증
        if (OrderStatus.NOT_PAID!=status) {
            throw new IllegalStateException("주문 상태가 결제 대기 상태가 아닙니다.");
        }

        // 3. 사용자 포인트 조회
        Point point = pointService.getOrCreatePoint(userId);
        int balance = point.getBalance();

        // 4. 포인트 정책 검증
        if (balance < totalAmount) {
            throw new IllegalArgumentException("포인트 잔액이 부족합니다.");
        }

        // 5. 포인트 차감 + 이력 저장
        pointService.use(userId, totalAmount);
        pointService.saveHistory(point,order.getTotalAmount(), PointUseStatus.USE);

        // 6. 주문 상태 PAID 처리
        orderService.markPaid(orderId);

        // 7. 주문 데이터 전송 (간략 처리)
        payService.sendOrderData(order);
    }
}
