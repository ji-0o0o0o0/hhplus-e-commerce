package kr.hhplus.be.server.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // 1-1 포인트 충전
    INVALID_USER_ID("E001", 400, "유효하지 않은 사용자 ID입니다."),
    CHARGE_AMOUNT_TOO_LOW("E002", 400, "충전 금액은 0 보다 커야 합니다."),
    CHARGE_AMOUNT_EXCEEDS_ONCE("E003", 409, "1회 최대 충전 금액을 초과했습니다."),
    CHARGE_AMOUNT_EXCEEDS_TOTAL("E004", 409, "누적 충전 가능 금액을 초과했습니다."),
    USER_NOT_FOUND("E005", 404, "사용자를 찾을 수 없습니다."),

    // 1-2 잔액 조회
    BALANCE_USER_ID_INVALID("E006", 400, "유효하지 않은 사용자 ID입니다."),

    // 2️ 상품 조회
    INTERNAL_SERVER_ERROR("E999", 500, "서버 내부 오류가 발생했습니다."),

    // 3️-1 쿠폰 목록 조회
    COUPON_USER_ID_INVALID("E007", 400, "유효하지 않은 사용자 ID입니다."),

    // 3️-2 쿠폰 발급
    INVALID_COUPON_ID("E008", 400, "유효하지 않은 쿠폰 ID입니다."),
    COUPON_NOT_FOUND("E009", 404, "쿠폰을 찾을 수 없습니다."),
    COUPON_OUT_OF_STOCK("E010", 409, "쿠폰이 모두 소진되었습니다."),
    COUPON_ALREADY_ISSUED("E011", 409, "이미 발급받은 쿠폰입니다."),

    // 4️-1 주문 생성
    INVALID_PRODUCT_ID("E012", 400, "유효하지 않은 상품 ID입니다."),
    PRODUCT_NOT_FOUND("E013", 404, "상품을 찾을 수 없습니다."),
    INVALID_ORDER_QUANTITY("E014", 400, "수량은 1 이상이어야 합니다."),
    PRODUCT_OUT_OF_STOCK("E015", 409, "재고가 부족합니다."),
    INVALID_ORDER_COUPON_ID("E016", 400, "유효하지 않은 쿠폰 ID입니다."),
    USER_COUPON_NOT_FOUND("E017", 409, "해당 쿠폰을 보유하고 있지 않습니다."),
    COUPON_EXPIRED("E018", 409, "쿠폰 유효기간이 지났거나 아직 유효하지 않습니다."),
    COUPON_ALREADY_USED("E019", 409, "이미 사용된 쿠폰입니다."),

    // 4️-2 결제 처리
    INVALID_ORDER_ID("E020", 400, "유효하지 않은 주문 ID입니다."),
    ORDER_NOT_FOUND("E021", 404, "주문을 찾을 수 없습니다."),
    INVALID_ORDER_STATUS("E022", 409, "결제가 불가능한 주문 상태입니다."),
    POINT_NOT_ENOUGH("E023", 409, "포인트가 부족합니다.");

    private final String code;
    private final int httpStatus;
    private final String message;
}
