package kr.hhplus.be.server.infrastructure.payment;

import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.paymant.PaymentRepository;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryPaymentRepository implements PaymentRepository {
    @Override
    public void extSave(Order order) {

    }
}
