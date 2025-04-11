package kr.hhplus.be.server.infrastructure.order;

import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class InMemoryOrderRepository implements OrderRepository {

    @Override
    public Order save(Order order) {

        return order;
    }

    @Override
    public Optional<Order> findById(Long orderId) {
        return Optional.empty();
    }

}