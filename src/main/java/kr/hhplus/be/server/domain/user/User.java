package kr.hhplus.be.server.domain.user;

public class User {
    private final Long userId;

    public User (Long id) {
        if ( id <= 0) throw new IllegalArgumentException("유효하지 않은 유저 아이디입니다.");
        this.userId = id;
    }

    public Long getUserId() { return userId; }
}