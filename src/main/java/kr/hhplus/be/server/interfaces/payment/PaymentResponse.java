package kr.hhplus.be.server.interfaces.payment;

import kr.hhplus.be.server.application.payment.PaymentResult;

public record PaymentResponse(Long orderId, String status) {

    public static PaymentResponse from(PaymentResult result) {
        return new PaymentResponse(result.orderId(), result.orderStatus().name());
    }
}
