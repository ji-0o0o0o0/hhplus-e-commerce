package kr.hhplus.be.server.domain.user;

public class User {
    private final Long id;

    public User (Long id) {
        if ( id <= 0) throw new IllegalArgumentException("유효하지 않은 유저 아이디입니다.");
        this.id = id;
    }

    public Long getId() { return id; }
}