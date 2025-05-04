package kr.hhplus.be.server.application.product;

import java.util.List;

public interface ProductUseCase {
    List<ProductResult> getAllProducts();
}