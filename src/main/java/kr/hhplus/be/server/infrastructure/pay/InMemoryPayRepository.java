package kr.hhplus.be.server.infrastructure.pay;

import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.pay.PayRepository;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryPayRepository implements PayRepository {
    @Override
    public void extSave(Order order) {

    }
}
