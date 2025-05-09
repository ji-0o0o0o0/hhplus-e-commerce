package kr.hhplus.be.server.domain.coupon;

import jakarta.persistence.*;
import kr.hhplus.be.server.common.exception.ApiException;
import kr.hhplus.be.server.common.exception.ErrorCode;
import kr.hhplus.be.server.domain.common.entity.AuditableEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

import static kr.hhplus.be.server.common.exception.ErrorCode.COUPON_OUT_OF_STOCK;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "coupon")
@Entity
public class Coupon extends AuditableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String couponName;
    private BigDecimal discountValue;
    @Enumerated(EnumType.STRING)
    private DiscountType discountType;
    private LocalDate startDate;
    private LocalDate endDate;
    private Long stock;

    public Coupon(String couponName, BigDecimal discountValue, DiscountType discountType, LocalDate startDate, LocalDate endDate, Long stock) {
        this.couponName = couponName;
        this.discountValue = discountValue;
        this.discountType = discountType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.stock = stock;
    }

    public static Coupon of(String couponName, BigDecimal discountValue, DiscountType discountType, LocalDate startDate, LocalDate endDate, Long stock) {
        return new Coupon(couponName, discountValue, discountType, startDate, endDate, stock);
    }

    //쿠폰 적용 금액
    public BigDecimal getDiscountAmount(BigDecimal totalAmount) {
        if (this.discountType == DiscountType.RATE) {
            return totalAmount.multiply(this.discountValue).divide(BigDecimal.valueOf(100));
        }
        return this.discountValue.min(totalAmount);
    }

    //쿠폰발급
    public void issuance() {
        if (this.stock <= 0) {
            throw new ApiException(COUPON_OUT_OF_STOCK);
        }
        this.stock -= 1;
    }


    //쿠폰 만료 여부
    public boolean isExpired() {
        return this.endDate.isBefore(LocalDate.now()) || this.startDate.isAfter(LocalDate.now());
    }
}
