package kr.hhplus.be.server.domain.point;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor(access= AccessLevel.PROTECTED)
public final class PointInfo {
    //PointInfo에 final을 붙이는 이유는 확장못하게(상속 금지)해서 vo의 용도를 명확하게 하기 위해
    //innerClass에는 static이 있는데 PointInfo 에는 static이 없는 이유 -> 파일 최상단 public class엔 static 불가(자바 규칙)
    //innnerClass 에 왜 static? -> 인스턴스 없이 독립 사용 가능하게 하기 위해

    /*포인트조회*/
    @Getter
    @Builder
    public static class Balance{
        private long pointId;
        private long userId;
        private BigDecimal balance;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public static Balance from(Point point){
            return Balance.builder()
                    .pointId(point.getId())
                    .userId(point.getUserId())
                    .balance(point.getBalance())
                    .createdAt(point.getCreatedAt())
                    .updatedAt(point.getUpdateAt())
                    .build();
        }

    }
    /*포인트 충전*/
    @Getter
    @Builder
    public static class Increase{
        private long pointId;
        private long userId;
        private BigDecimal balance;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public static Increase from(Point point){
            return Increase.builder()
                    .pointId(point.getId())
                    .userId(point.getUserId())
                    .balance(point.getBalance())
                    .createdAt(point.getCreatedAt())
                    .updatedAt(point.getUpdateAt())
                    .build();
        }
    }

    /*포인트 차감*/
    @Getter
    @Builder
    public static class Decrease{
        private long pointId;
        private long userId;
        private BigDecimal balance;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public static Decrease from(Point point){
            return Decrease.builder()
                    .pointId(point.getId())
                    .userId(point.getUserId())
                    .balance(point.getBalance())
                    .createdAt(point.getCreatedAt())
                    .updatedAt(point.getUpdateAt())
                    .build();
        }
    }
    /*사용자의 포인트 사용/충전 이력 조회*/
    @Getter
    @Builder
    public static class History{
        private long pointHistoryId;
        private long pointId;
        private long userId;
        private BigDecimal amount;
        private BigDecimal balance;
        private PointUseStatus type;
        private LocalDateTime createdAt;

        public static History from(PointHistory pointHistory){
            return History.builder()
                    .pointHistoryId(pointHistory.getId())
                    .pointId(pointHistory.getPointId())
                    .userId(pointHistory.getUserId())
                    .amount(pointHistory.getAmount())
                    .balance(pointHistory.getBalance())
                    .type(pointHistory.getType())
                    .createdAt(pointHistory.getCreatedAt())
                    .build();
        }
    }
}

