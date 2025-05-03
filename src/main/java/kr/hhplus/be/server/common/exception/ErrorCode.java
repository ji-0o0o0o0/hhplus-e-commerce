package kr.hhplus.be.server.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    USER_NOT_FOUND("USER_NOT_FOUND", 404, "사용자를 찾을 수 없습니다."),
    INVALID_USER("INVALID_USER", 400, "유효하지 않은 사용자 입니다."),
    INVALID_CHARGE_AMOUNT("INVALID_CHARGE_AMOUNT", 400, "유효하지 않은 충전 금액입니다."),
    INVALID_USE_AMOUNT("INVALID_USE_AMOUNT", 400, "유효하지 않은 사용 금액입니다."),

    CHARGE_AMOUNT_EXCEEDS_ONCE("CHARGE_AMOUNT_EXCEEDS_ONCE", 409, "1회 최대 충전 금액을 초과했습니다."),
    CHARGE_AMOUNT_EXCEEDS_TOTAL("CHARGE_AMOUNT_EXCEEDS_TOTAL", 409, "누적 충전 가능 금액을 초과했습니다."),
    POINT_AMOUNT_DECREASE_EXCEEDS_LIMIT("POINT_AMOUNT_DECREASE_EXCEEDS_LIMIT",409,"차감 금액이 최대 포인트 한도를 초과하였습니다."),


    INVALID_COUPON("INVALID_COUPON", 400, "유효하지 않은 쿠폰 입니다."),
    COUPON_NOT_FOUND("COUPON_NOT_FOUND", 404, "쿠폰을 찾을 수 없습니다."),
    COUPON_OUT_OF_STOCK("COUPON_OUT_OF_STOCK", 409, "쿠폰이 모두 소진되었습니다."),
    COUPON_ALREADY_ISSUED("COUPON_ALREADY_ISSUED", 409, "이미 발급받은 쿠폰입니다."),

    INVALID_PRODUCT_ID("INVALID_PRODUCT_ID", 400, "유효하지 않은 상품입니다."),
    PRODUCT_NOT_FOUND("PRODUCT_NOT_FOUND", 404, "상품을 찾을 수 없습니다."),
    INVALID_ORDER_QUANTITY("INVALID_ORDER_QUANTITY", 400, "수량은 1 이상이어야 합니다."),
    PRODUCT_OUT_OF_STOCK("PRODUCT_OUT_OF_STOCK", 409, "재고가 부족합니다."),
    USER_COUPON_NOT_FOUND("USER_COUPON_NOT_FOUND", 409, "해당 쿠폰을 보유하고 있지 않습니다."),
    INVALID_USER_COUPON("INVALID_USER_COUPON", 404, "유효하지 않은 사용자 쿠폰입니다."),
    COUPON_EXPIRED("COUPON_EXPIRED", 409, "쿠폰 유효기간이 지났습니다."),
    COUPON_ALREADY_USED("COUPON_ALREADY_USED", 409, "이미 사용된 쿠폰입니다."),


    // 4️-2 결제 처리
    INVALID_ORDER("INVALID_ORDER_ID", 400, "유효하지 않은 주문 입니다."),
    INVALID_ORDER_USER("INVALID_ORDER_USER", 400, "주문과 주문자 정보와 일치하지 않습니다."),
    INVALID_ORDER_PRODUCT("INVALID_ORDER_PRODUCT", 400, "주문할 상품이 없습니다."),
    ORDER_NOT_FOUND("ORDER_NOT_FOUND", 404, "주문을 찾을 수 없습니다."),
    INVALID_ORDER_STATUS("INVALID_ORDER_STATUS", 409, "결제가 불가능한 주문 상태입니다."),
    POINT_NOT_ENOUGH("POINT_NOT_ENOUGH", 409, "포인트가 부족합니다."),

    INVALID_RESTORE_AMOUNT("INVALID_RESTORE_AMOUNT", 400, "재고 복원 수량이 0 이하일 수 없습니다."),

    INTERNAL_SERVER_ERROR("INTERNAL_SERVER_ERROR", 500, "서버 내부 오류가 발생했습니다.");

    private final String code;
    private final int httpStatus;
    private final String message;
}
