package kr.hhplus.be.server.domain.paymant;

import kr.hhplus.be.server.domain.order.Order;

public interface PaymentRepository {
     void extSave(Order order);
}
