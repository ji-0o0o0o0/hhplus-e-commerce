package kr.hhplus.be.server.domain.order;

import kr.hhplus.be.server.domain.common.entity.AuditableEntity;
import kr.hhplus.be.server.domain.product.Product;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem extends AuditableEntity {
    //상품id, 가격, 수량
    //가격 * 수량 책임

    private Long id;
    private Long orderId;
    private Long productId;
    private String productName;
    private BigDecimal price;
    private Long quantity;

    public OrderItem(Long orderId, Long productId, String productName, BigDecimal price, Long quantity) {
        this.orderId = orderId;
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
    }

    public static OrderItem create(Product product, Long quantity) {
        return new OrderItem(null,product.getId(), product.getName(), product.getPrice(), quantity);
    }

    // 갯수에 따른 제품 가격
    public BigDecimal calculateAmount(){
        return this.price.multiply(BigDecimal.valueOf(this.quantity));
    }
    //제고 복원
    public void restoreStock(Product product) {
        product.increaseStock(this.quantity);
    }
}
