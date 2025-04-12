package kr.hhplus.be.server.domain.order;

import java.util.List;
import java.util.Map;

public class Order {
    //Long	DB에서 PK로 자주 쓰이고, null 가능성이 있음
    private Long orderId;
    private final Long userId;
    private final List<OrderItem> orderItems;
    private final boolean isCouponApplied;
    private final Long userCouponId;//null가능
    private final int totalAmount;
    private OrderStatus orderStatus;

    public Order(Long userId, List<OrderItem> orderItems, Long userCouponId, Map<String, Double> couponDiscountInfo) {
        //유저랑 주문유무 검증
        if(userId < 0) throw new IllegalArgumentException("올바르지 않은 유저입니다.");
        if(orderItems.isEmpty()||orderItems==null) throw new IllegalArgumentException("주문 항목이 없습니다.");


        this.userId = userId;
        this.orderItems = orderItems;
        this.isCouponApplied = (userCouponId != null);
        this.userCouponId = userCouponId;
        this.totalAmount = calculateDiscountedAmount(couponDiscountInfo);
        this.orderStatus = OrderStatus.NOT_PAID; //결제 전
    }
    public Order(Long userId, List<OrderItem> orderItems, Long userCouponId) {
        this(userId, orderItems, userCouponId, null);
    }

    //총합
    public int calculateTotalAmount(){
        return orderItems.stream().mapToInt(OrderItem::calculateAmount).sum();
    }
    //할인율 적용
    private int calculateDiscountedAmount(Map<String, Double> couponDiscountInfo) {
        int total = calculateTotalAmount();

        if (couponDiscountInfo == null || couponDiscountInfo.isEmpty()) return total;

        if (couponDiscountInfo.containsKey("RATE")) {
            double rate = couponDiscountInfo.get("RATE"); // ex: 10.0
            return (int) Math.floor(total * (1 - rate / 100.0));
        }

        if (couponDiscountInfo.containsKey("AMOUNT")) {
            double amount = couponDiscountInfo.get("AMOUNT");
            return (int) Math.max(0, total - amount); // 최소 0 이상
        }

        return total;
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

    public Long getOrderId() {
        return orderId;
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
}
