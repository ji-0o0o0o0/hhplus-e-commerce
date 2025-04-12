package kr.hhplus.be.server.interfaces.pay;

import kr.hhplus.be.server.application.pay.PayCommand;

public record PayRequest(Long orderId) {

    public PayCommand toCommand() {
        if (orderId == null || orderId <= 0) {
            throw new IllegalArgumentException("유효하지 않은 주문 ID입니다.");
        }
        return new PayCommand(orderId);
    }
}
