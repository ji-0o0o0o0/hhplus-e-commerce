package kr.hhplus.be.server.domain.coupon;

import java.time.LocalDate;

public class Coupon {
    private final Long id;
    private final String couponName;
    private final double discountValue;
    private final String discountType;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final int stock;

    public Coupon(Long id, String couponName, double discountValue, String discountType,
                  LocalDate startDate, LocalDate endDate, int stock) {
        this.id = id;
        this.couponName = couponName;
        this.discountValue = discountValue;
        this.discountType = discountType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.stock = stock;
    }

    public Long getId() { return id; }
    public String getCouponName() { return couponName; }
    public double getDiscountValue() { return discountValue; }
    public String getDiscountType() { return discountType; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
    public int getStock() { return stock; }

    public boolean isExpired(LocalDate today) {
        return today.isBefore(startDate) || today.isAfter(endDate);
    }
}
