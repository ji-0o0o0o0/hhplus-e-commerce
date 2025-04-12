package kr.hhplus.be.server.domain.pay;

import kr.hhplus.be.server.domain.order.Order;

public interface PayRepository {
     void extSave(Order order);
}
