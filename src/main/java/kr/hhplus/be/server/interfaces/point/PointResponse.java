package kr.hhplus.be.server.interfaces.point;

import kr.hhplus.be.server.application.point.PointResult;
import kr.hhplus.be.server.domain.point.PointUseStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PointResponse() {
    public record UserPoint(
            long userId,
            BigDecimal balance

    ) {
        public static UserPoint from(PointResult.UserPoint pointResult) {
            return new UserPoint(
                    pointResult.getUserId(),
                    pointResult.getBalance()
            );
        }
    }
    public record Charge(
            long id,
            long userId,
            BigDecimal balance,
            LocalDateTime createdAt,
            LocalDateTime lastModifiedAt
    ) {
        public static Charge from(PointResult.Charge charge) {
            return new Charge(
                    charge.getPointId(),
                    charge.getUserId(),
                    charge.getBalance(),
                    charge.getCreatedAt(),
                    charge.getLastModifiedAt()
            );
        }
    }
    public record History(
            long pointHistoryId,
            long userId,
            BigDecimal amount,
            BigDecimal balance,
            PointUseStatus type,
            LocalDateTime createdAt
    ) {
        public static History from(PointResult.History history) {
            return new History(
                    history.getPointHistoryId(),
                    history.getUserId(),
                    history.getAmount(),
                    history.getBalance(),
                    history.getType(),
                    history.getCreatedAt()
            );
        }
    }
}
