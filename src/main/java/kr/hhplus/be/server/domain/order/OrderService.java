package kr.hhplus.be.server.domain.order;


import kr.hhplus.be.server.common.exception.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static kr.hhplus.be.server.common.exception.ErrorCode.*;

@RequiredArgsConstructor
@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderExternalClient orderExternalClient;

    //주문 생성
    public Order order(Order order){
        if(order.getOrderItems().isEmpty()) throw new ApiException(INVALID_ORDER_PRODUCT);
        return orderRepository.save(order);
    }
    //주문 결제 완료 처리
    public void completeOrder(Order order){
        order.markPaid();
        orderRepository.save(order);
        // 외부 플랫폼 데이터 전송
        orderExternalClient.sendOrder(order);
    }

    //주문 만료 처리
    public void expireOrder(Order order){
        order.markExpired();
        orderRepository.save(order);
    }
    //주문 id로 주문 조회
    public Order getOrder(Long orderId) {
        return orderRepository.findByOrderId(orderId)
                .orElseThrow(() -> new ApiException(INVALID_ORDER));
    }
}