package kr.hhplus.be.server.domain.order;

public enum OrderStatus {
    //주문 상태는 enum으로 관리
    NOT_PAID,
    PAID,
    CANCEL,
    EXPIRED
}
