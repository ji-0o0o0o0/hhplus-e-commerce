package kr.hhplus.be.server.domain.point;


import kr.hhplus.be.server.common.exception.ApiException;
import kr.hhplus.be.server.common.exception.ErrorCode;
import kr.hhplus.be.server.domain.common.entity.AuditableEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static kr.hhplus.be.server.common.exception.ErrorCode.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Point extends AuditableEntity {
    private static final int MAX_CHARGE_ONCE = 1_000_000;
    private static final int MAX_CHARGE_TOTAL = 5_000_000;

    private Long id;
    private Long userId;
    private Long balance;

    public Point(Long userId, Long initialBalance) {
        this.userId = userId;
        this.balance = initialBalance;
    }

    public static Point of(Long userId, Long balance) {
        return new Point(userId, balance);
    }


    /*충전*/
    public void  increase(Long amount){
        if (amount <= 0) throw new ApiException(INVALID_CHARGE_AMOUNT);

        if (amount > MAX_CHARGE_ONCE) throw new ApiException(CHARGE_AMOUNT_EXCEEDS_ONCE);

        long newAmount = this.balance + amount;
        if (newAmount > MAX_CHARGE_TOTAL) throw new ApiException(CHARGE_AMOUNT_EXCEEDS_TOTAL);

        this.balance += amount;
    }
    /*사용*/
    public void decrease(Long amount){
        //1. 0 이하인 경우
        if (amount <= 0) throw new ApiException(INVALID_USE_AMOUNT);
        //2. 차감 금액이 최대 포인트 한도를 초과한 경우
        if (amount > MAX_CHARGE_TOTAL) throw new ApiException(POINT_AMOUNT_DECREASE_EXCEEDS_LIMIT);
        //3. 차감 금액이 현재 잔액보다 큰 경우
        if (amount > balance) throw new ApiException(POINT_NOT_ENOUGH);

        this.balance -= amount;
    }

}

