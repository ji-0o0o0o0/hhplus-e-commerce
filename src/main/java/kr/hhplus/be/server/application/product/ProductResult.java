package kr.hhplus.be.server.application.product;


import kr.hhplus.be.server.domain.product.Product;

import java.math.BigDecimal;

public record ProductResult(
        Long id,
        String name,
        BigDecimal price,
        Long stock
) {
    public static ProductResult from(Product product) {
        return new ProductResult(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getStock()
        );
    }
}

