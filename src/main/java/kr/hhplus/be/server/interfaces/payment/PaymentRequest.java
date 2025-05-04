package kr.hhplus.be.server.interfaces.payment;

import kr.hhplus.be.server.application.payment.PaymentCommand;

public record PaymentRequest(Long userId, Long orderId) {
    public PaymentCommand toCommand() {
        return new PaymentCommand(userId, orderId);
    }
}
