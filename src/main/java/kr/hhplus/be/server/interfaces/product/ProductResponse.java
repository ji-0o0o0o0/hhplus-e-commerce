package kr.hhplus.be.server.interfaces.product;



import kr.hhplus.be.server.application.product.ProductResult;

public record ProductResponse(
        Long id,
        String name,
        int price,
        int stock
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
