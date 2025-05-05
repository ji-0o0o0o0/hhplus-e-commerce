package kr.hhplus.be.server.infrastructure.product;

import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class InMemoryProductRepository implements ProductRepository {


    @Override
    public Optional<Product> findById(Long productId) {
        return Optional.empty();
    }

    @Override
    public List<Product> findAll() {
        return null;
    }
}