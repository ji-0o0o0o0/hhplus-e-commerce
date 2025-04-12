package kr.hhplus.be.server.domain.product;


import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public Product getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 상품을 찾을 수 없습니다."));
        // 지금 주문 가능한지 확인
        if (product.getStock() <= 0) {
            throw new IllegalStateException("재고 없음");
        }
        return product;
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }
}