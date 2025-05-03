package kr.hhplus.be.server.domain.paymant;

import kr.hhplus.be.server.domain.order.Order;

public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public void sendOrderData(Order order) {
        //결제 완료 후 데이터 플랫폼 전송
        paymentRepository.extSave(order);
    }
}
