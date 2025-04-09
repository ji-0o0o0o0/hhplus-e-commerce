package kr.hhplus.be.server.domain.product;


import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public Product getProductById(Long id) {
        Product product = productRepository.getById(id);
        // 지금 주문 가능한지 확인
        if (product.getStock() <= 0) {
            throw new IllegalStateException("재고 없음");
        }
        return product;
    }
}