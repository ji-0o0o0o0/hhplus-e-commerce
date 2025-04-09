package kr.hhplus.be.server.domain.point;


public class UserPoint {
    private final Long userId;
    private Long balance;
    private Long totalCharged;

    public UserPoint(Long userId, Long balance, Long totalCharged) {
        this.userId = userId;
        this.balance = 0L;
        this.totalCharged = 0L;
    }

    //충전 하면 누적
    public  void charge(long amount){
        this.balance += amount;
        this.totalCharged += amount;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getBalance() {
        return balance;
    }

    public Long getTotalCharged() {
        return totalCharged;
    }
}
