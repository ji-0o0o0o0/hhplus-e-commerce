package kr.hhplus.be.server.domain.order;

import kr.hhplus.be.server.domain.order.Order;

public interface OrderRepository {
    void save(Order order);
}
