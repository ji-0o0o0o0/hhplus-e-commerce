package kr.hhplus.be.server.domain.pay;

import kr.hhplus.be.server.domain.order.Order;

public class PayService {

    private final PayRepository payRepository;

    public PayService(PayRepository payRepository) {
        this.payRepository = payRepository;
    }

    public void sendOrderData(Order order) {
        //결제 완료 후 데이터 플랫폼 전송
        payRepository.extSave(order);
    }
}
