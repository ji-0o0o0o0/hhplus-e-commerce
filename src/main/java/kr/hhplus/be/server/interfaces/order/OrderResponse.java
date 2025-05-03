package kr.hhplus.be.server.interfaces.order;

import kr.hhplus.be.server.application.order.OrderResult;

public record OrderResponse(Long orderId) {
    // from() 메서드는 application 결과 DTO를 interface 응답 DTO로 변환합니다.
    public static OrderResponse from(OrderResult result) {
        return new OrderResponse(result.orderId());
    }
}