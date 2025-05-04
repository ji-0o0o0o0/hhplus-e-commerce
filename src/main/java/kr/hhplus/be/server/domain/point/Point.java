package kr.hhplus.be.server.domain.point;


import kr.hhplus.be.server.common.exception.ApiException;
import kr.hhplus.be.server.domain.common.entity.AuditableEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

import static kr.hhplus.be.server.common.exception.ErrorCode.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Point extends AuditableEntity {
    private static final int MAX_CHARGE_ONCE = 1_000_000;
    private static final int MAX_CHARGE_TOTAL = 5_000_000;

    private Long id;
    private Long userId;
    private BigDecimal balance;

    public Point(Long userId, BigDecimal initialBalance) {
        this.userId = userId;
        this.balance = initialBalance;
    }

    public static Point of(Long userId, BigDecimal balance) {
        return new Point(userId, balance);
    }


    /*충전*/
    public void increase(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0)
            throw new ApiException(INVALID_CHARGE_AMOUNT);

        if (amount.compareTo(BigDecimal.valueOf(MAX_CHARGE_ONCE)) > 0)
            throw new ApiException(CHARGE_AMOUNT_EXCEEDS_ONCE);

        BigDecimal newAmount = this.balance.add(amount);
        if (newAmount.compareTo(BigDecimal.valueOf(MAX_CHARGE_TOTAL)) > 0)
            throw new ApiException(CHARGE_AMOUNT_EXCEEDS_TOTAL);

        this.balance = newAmount;
    }
    /*사용*/
    public void decrease(BigDecimal amount){
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0)
            throw new ApiException(INVALID_USE_AMOUNT);

        if (amount.compareTo(BigDecimal.valueOf(MAX_CHARGE_TOTAL)) > 0)
            throw new ApiException(POINT_AMOUNT_DECREASE_EXCEEDS_LIMIT);

        if (amount.compareTo(balance) > 0)
            throw new ApiException(POINT_NOT_ENOUGH);

        this.balance = this.balance.subtract(amount);
    }

    public PointHistory charge(BigDecimal amount) {
        increase(amount);
        return PointHistory.saveHistory(this, amount, PointUseStatus.CHARGE);
    }

    public PointHistory use(BigDecimal amount) {
        decrease(amount);
        return PointHistory.saveHistory(this, amount, PointUseStatus.USE);
    }


}

