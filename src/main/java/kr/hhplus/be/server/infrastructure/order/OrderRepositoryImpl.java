package kr.hhplus.be.server.infrastructure.order;

import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderRepository;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepositoryImpl implements OrderRepository {

    @Override
    public void save(Order order) {
        // 지금은 저장 X, 나중에 JPA 붙이면 여기에 로직 추가
        System.out.println("Order 저장됨 (mock): " + order);
    }

}