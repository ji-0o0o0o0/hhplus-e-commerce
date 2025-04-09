package kr.hhplus.be.server.interfaces.product;

import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductRepository;
import org.springframework.stereotype.Repository;

@Repository
public class ProductRepositoryImpl implements ProductRepository {
    @Override
    public Product getById(Long productId) {
        // 지금은 DB 미사용: mock 반환
        return new Product(productId, "테스트상품", 1000, 5);
    }
}