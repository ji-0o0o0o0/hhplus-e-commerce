package kr.hhplus.be.server.domain.point;

import java.time.LocalDate;

public class PointHistory {
    private final Long pointId;
    private final int amount;
    private final int balance;
    private PointUseStatus  type;
    private LocalDate createdAt;

    public PointHistory(Long pointId, int amount, int balance, PointUseStatus type) {
        this.pointId = pointId;
        this.amount = amount;
        this.balance = balance;
        this.type = type;
        this.createdAt = LocalDate.now();
    }
}