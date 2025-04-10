package kr.hhplus.be.server.domain.order;

import java.util.List;

public class Order {
    //Long	DB에서 PK로 자주 쓰이고, null 가능성이 있음
    private final Long userId;
    private final List<OrderItem> orderItems;
    private final boolean isCouponApplied;
    private final Long userCouponId;//null가능
    private final int totalAmount;
    private OrderStatus orderStatus;
    private Long orderId;

    public Order(Long userId, List<OrderItem> orderItems, Long userCouponId) {
        //유저랑 주문유무 검증
        if(userId < 0) throw new IllegalArgumentException("올바르지 않은 유저입니다.");
        if(orderItems.isEmpty()||orderItems==null) throw new IllegalArgumentException("주문 항목이 없습니다.");


        this.userId = userId;
        this.orderItems = orderItems;
        this.isCouponApplied = (userCouponId != null);
        this.userCouponId = userCouponId;
        this.totalAmount = calculateTotalAmount();
        this.orderStatus = OrderStatus.NOT_PAID; //결제 전
    }

    //총합
    public int calculateTotalAmount(){
        return orderItems.stream().mapToInt(OrderItem::calculateAmount).sum();
    }

    public void markPaid(){
        //결제 상태확인
        if(this.orderStatus==OrderStatus.NOT_PAID)throw new IllegalArgumentException("결제할 수 없는 상태입니다.");
        this.orderStatus = OrderStatus.PAID;
    }
    public void markExpired(){
        //만료 가능여부 확인
        if(this.orderStatus==OrderStatus.NOT_PAID)throw new IllegalArgumentException("결제할 수 없는 상태입니다.");
        this.orderStatus = OrderStatus.EXPIRED;
    }
    public void markCanceled(){
        //취소 가능여부 확인
        if(this.orderStatus==OrderStatus.NOT_PAID)throw new IllegalArgumentException("결제할 수 없는 상태입니다.");
        this.orderStatus = OrderStatus.CANCEL;
    }

    public Long getUserId() {
        return userId;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public boolean isCouponApplied() {
        return isCouponApplied;
    }

    public Long getUserCouponId() {
        return userCouponId;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public Long getOrderId() {
        return this.orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
}
