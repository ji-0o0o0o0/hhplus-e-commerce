package kr.hhplus.be.server.domain.product;

public interface ProductRepository {
    Product getById(Long productId);
}