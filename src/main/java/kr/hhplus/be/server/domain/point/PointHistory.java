package kr.hhplus.be.server.domain.point;

import kr.hhplus.be.server.domain.common.entity.AuditableEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PointHistory extends AuditableEntity {
    private Long id;
    private Long pointId;
    private Long userId;
    private BigDecimal amount;
    private BigDecimal balance;
    private PointUseStatus  type;

    public PointHistory(Long pointId,Long userId, BigDecimal amount, BigDecimal balance, PointUseStatus type) {
        this.pointId = pointId;
        this.userId = userId;
        this.amount = amount;
        this.balance = balance;
        this.type = type;
    }

    /*포인트 사용 이력 저장*/
    public static PointHistory saveHistory(Point point, BigDecimal amount, PointUseStatus type){
        return new PointHistory(point.getId(),point.getUserId(), amount,point.getBalance(),type);
    }
}