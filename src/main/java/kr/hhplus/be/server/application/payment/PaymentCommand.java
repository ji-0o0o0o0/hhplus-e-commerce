package kr.hhplus.be.server.application.payment;

public record PaymentCommand(Long userId, Long orderId) {
}