package kr.hhplus.be.server.domain.order;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    public Order save(Order order) {
        orderRepository.save(order);
        return order;
    }
}