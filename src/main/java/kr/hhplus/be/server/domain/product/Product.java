package kr.hhplus.be.server.domain.product;


import kr.hhplus.be.server.common.exception.ApiException;
import kr.hhplus.be.server.domain.common.entity.AuditableEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

import static kr.hhplus.be.server.common.exception.ErrorCode.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends AuditableEntity {

    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Long   stock;

    public Product(String name,String description, BigDecimal price, Long   stock) {
        if (name == null || name.isBlank()) throw new ApiException(INVALID_PRODUCT_ID);
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) throw new ApiException(INVALID_PRODUCT_ID);
        if (stock == null || stock < 0) throw new ApiException(PRODUCT_OUT_OF_STOCK);

        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
    }

    //주문생성
    public static Product create(String productName, String description, BigDecimal price, Long   stock) {
       return new Product(productName, description, price, stock);
    }

    //재고 차감
    public void decreaseStock(Long quantity) {
        if (quantity <= 0) throw new ApiException(INVALID_ORDER_QUANTITY);
        if (this.stock == null || this.stock < 0 ||this.stock < quantity) throw new ApiException(PRODUCT_OUT_OF_STOCK);

        this.stock -= quantity;
    }

    //재고 복원
    public void increaseStock(Long quantity) {
        if (quantity <= 0) throw new ApiException(INVALID_RESTORE_AMOUNT);
        this.stock += quantity;
    }
}
