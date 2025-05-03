package kr.hhplus.be.server.domain.order;

import kr.hhplus.be.server.common.exception.ApiException;
import kr.hhplus.be.server.common.exception.ErrorCode;
import kr.hhplus.be.server.domain.common.entity.AuditableEntity;
import kr.hhplus.be.server.domain.coupon.UserCoupon;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import static kr.hhplus.be.server.common.exception.ErrorCode.*;
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends AuditableEntity {
    //Long	DB에서 PK로 자주 쓰이고, null 가능성이 있음
    private Long id;
    private Long userId;
    private Long userCouponId;//null가능
    private boolean isCouponApplied;
    private BigDecimal totalAmount;
    private BigDecimal discountedAmount;
    private OrderStatus status;

    private final List<OrderItem> orderItems = new ArrayList<>();
    //-> 초기화 안전성 때문(생성자 호출 전에 orderItems는 무조건 비어 있는 리스트로 초기화)
    //바로 안전하게 사용할 수 있음. (NPE 방지)

    private Order(Long userId) {
        this.userId = userId;
        this.userCouponId = null;
        this.isCouponApplied = false;
        this.totalAmount = BigDecimal.ZERO;
        this.discountedAmount = BigDecimal.ZERO;
        this.status = OrderStatus.NOT_PAID;
    }

    //주문생성
    // static인 이유 -> 객체를 새로 만들기 위한 "팩토리 메소드" 이기 때문
    public static Order create(User user){
        if(user ==null) throw new ApiException(INVALID_USER);
        return new Order(user.getId());
    }

    // 주문 목록 추가
    public void addOrderItem(Product product, Long quantity){
       if(quantity==null || quantity <=0) throw new ApiException(INVALID_ORDER_QUANTITY);
       if (product==null) throw new ApiException(PRODUCT_NOT_FOUND);
       if (product.getStock()<=0) throw new ApiException(PRODUCT_OUT_OF_STOCK);

        OrderItem orderItem = OrderItem.create(product,quantity);
        this.orderItems.add(orderItem);
        this.totalAmount = this.totalAmount.add(product.getPrice().multiply(BigDecimal.valueOf(quantity)));
    }

    //쿠폰 적용
    public void applyCoupon(UserCoupon userCoupon){
        if(this.isCouponApplied) throw new ApiException(COUPON_ALREADY_USED);
        if (!userCoupon.isAvailable()) throw new ApiException(INVALID_COUPON);

        this.userCouponId = userCoupon.getId();
        this.discountedAmount = this.totalAmount.subtract(userCoupon.getCoupon().getDiscountAmount(this.totalAmount));
        this.isCouponApplied = true;
        userCoupon.markAsUsed();
    }

    public void markPaid(){
        //결제 상태확인
        if(this.status!=OrderStatus.NOT_PAID)throw new ApiException(INVALID_ORDER_STATUS);
        this.status = OrderStatus.PAID;
    }
    public void markExpired(){
        //만료 가능여부 확인
        if(this.status!=OrderStatus.NOT_PAID)throw new ApiException(INVALID_ORDER_STATUS);
        this.status = OrderStatus.EXPIRED;
    }
    public void markCanceled(){
        //취소 가능여부 확인
        if(this.status!=OrderStatus.NOT_PAID)throw new ApiException(INVALID_ORDER_STATUS);
        this.status = OrderStatus.CANCEL;
    }

}
