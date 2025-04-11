package kr.hhplus.be.server.domain.point;


public class Point {
    private static final int MAX_CHARGE_ONCE = 1_000_000;
    private static final int MAX_CHARGE_TOTAL = 5_000_000;

    private Long pointId;
    private final Long userId;
    private int balance;
    private int totalCharged;

    public Point(Long userId) {
        this.pointId = pointId;
        this.userId = userId;
        this.balance = 0;
        this.totalCharged = 0;
    }

    //포인트 충전
    public void charge(int amount) {
        if (amount <= 0 || amount > MAX_CHARGE_ONCE) {
            throw new IllegalArgumentException("1회 충전 금액은 0보다 크고 1,000,000 이하여야 합니다.");
        }
        if (totalCharged + amount > MAX_CHARGE_TOTAL) {
            throw new IllegalArgumentException("누적 충전 금액은 5,000,000원을 초과할 수 없습니다.");
        }
        this.balance += amount;
        this.totalCharged += amount;
    }

    //포인트 사용
    public void use(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("차감할 포인트는 0보다 커야 합니다.");
        }

        if (this.balance < amount) {
            throw new IllegalStateException("포인트 잔액이 부족합니다.");
        }

        this.balance -= amount;
    }

    public Long getPointId() {
        return pointId;
    }

    public Long getUserId() {
        return userId;
    }

    public int getBalance() {
        return balance;
    }

    public int getTotalCharged() {
        return totalCharged;
    }



}

