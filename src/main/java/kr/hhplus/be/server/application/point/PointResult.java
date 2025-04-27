package kr.hhplus.be.server.application.point;

import kr.hhplus.be.server.domain.point.PointInfo;
import kr.hhplus.be.server.domain.point.PointUseStatus;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PointResult {

    @Getter
    @Builder
    public static class UserPoint {
        private long userId;
        private long balance;

        public static UserPoint from(PointInfo.Balance info) {
            return UserPoint.builder()
                    .userId(info.getUserId())
                    .balance(info.getBalance())
                    .build();
        }
    }

    @Getter
    @Builder
    public static class Charge {
        private long pointId;
        private long userId;
        private long balance;
        private LocalDateTime createdAt;
        private LocalDateTime lastModifiedAt;

        public static PointResult.Charge from(PointInfo.Increase info) {
            return PointResult.Charge.builder()
                    .pointId(info.getPointId())
                    .userId(info.getUserId())
                    .balance(info.getBalance())
                    .createdAt(info.getCreatedAt())
                    .lastModifiedAt(info.getUpdatedAt())
                    .build();
        }
    }

    @Getter
    @Builder
    public static class History {
        private long pointHistoryId;
        private long userId;
        private long amount;
        private long balance;
        private PointUseStatus type;
        private LocalDateTime createdAt;

        public static PointResult.History from(PointInfo.History info) {
            return History.builder()
                    .pointHistoryId(info.getPointId())
                    .userId(info.getUserId())
                    .amount(info.getAmount())
                    .balance(info.getBalance())
                    .type(info.getType())
                    .createdAt(info.getCreatedAt())
                    .build();
        }
    }

    @Getter
    @Builder
    public static class Payment {
        private long userId;
        private long amount;
    }

}