package kr.hhplus.be.server.domain.product;


public class Product {
    private final Long productId;
    private final String productName;
    private final int price;
    private final int stock;

    public Product(Long productId, String productName, int price, int stock) {

        if(productId < 0) throw new IllegalArgumentException("유효하지 않은 상품입니다.");
        if(price<0) throw new IllegalArgumentException("가격이 유효하지 않습니다.");

        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.stock = stock;
    }

    //재고 차감
    public Product deductStock(int quantity) {
        if (this.stock < quantity) {
            throw new IllegalStateException("재고가 부족합니다.");
        }
        return new Product(this.productId, this.productName, this.price, this.stock - quantity);
    }
    //재고 롤백
    public Product  rollbackStock(int quantity) {
        if (quantity <= 0) throw new IllegalArgumentException("롤백 수량은 0보다 커야 합니다.");
        return new Product(this.productId, this.productName, this.price, this.stock + quantity);
    }


    public Long getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public int getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }
}
