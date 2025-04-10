package kr.hhplus.be.server.infrastructure.product;

import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ProductRepositoryImpl implements ProductRepository {

    @Override
    public Optional<Product> findById(Long productId) {
        return Optional.of(new Product(productId, "테스트상품", 1000, 5));
    }

    @Override
    public List<Product> findAll() {
        return List.of();
    }
}