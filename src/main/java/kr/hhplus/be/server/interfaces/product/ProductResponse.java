package kr.hhplus.be.server.interfaces.product;



import kr.hhplus.be.server.application.product.ProductResult;

import java.math.BigDecimal;

public record ProductResponse(
        Long id,
        String name,
        BigDecimal price,
        Long stock
) {
    public static ProductResponse fromResult(ProductResult result) {
        return new ProductResponse(
                result.id(),
                result.name(),
                result.price(),
                result.stock()
        );
    }
}
