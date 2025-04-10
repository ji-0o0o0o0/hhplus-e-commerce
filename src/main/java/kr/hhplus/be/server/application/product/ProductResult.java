package kr.hhplus.be.server.application.product;


import kr.hhplus.be.server.domain.product.Product;

public record ProductResult(
        Long id,
        String name,
        int price,
        int stock
) {
    public static ProductResult from(Product product) {
        return new ProductResult(
                product.getProductId(),
                product.getProductName(),
                product.getPrice(),
                product.getStock()
        );
    }
}

