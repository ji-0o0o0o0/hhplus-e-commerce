package kr.hhplus.be.server.domain.order;

public class OrderItem {
    //상품id, 가격, 수량
    //가격 * 수량 책임

    private Long productId;
    private int price;
    private int quantity;

    public OrderItem(Long productId, int price, int quantity) {
        //가격 * 수량 책임
        // 가격 0 미만일 경우 에러
        if(price < 0) throw new IllegalArgumentException("가격은 0원 이상이어야합니다.");
        // 수량 1개 이상
        if(quantity < 1) throw new IllegalArgumentException("수량은 1개 이상이어야합니다.");

        this.productId = productId;
        this.price = price;
        this.quantity = quantity;
    }

    // 갯수에 따른 제품 가격
    public int calculateAmount(){
        return price * quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public int getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }
}
