package kr.hhplus.be.server.lagacy.order.docs;

public final class OrderSwaggerDocs {

    // ✅ 주문 성공
    public static final String ORDER_SUCCESS = """
        {
          "code": 201,
          "status": "Created",
          "message": "요청이 정상적으로 처리되었습니다.",
          "data": {
            "orderId": 1001,
            "totalAmount": 25000,
            "finalAmount": 20000,
            "status": "PENDING"
          }
        }
    """;

    public static final String OUT_OF_STOCK = """
        {
          "code": 409,
          "message": "비즈니스 정책을 위반한 요청입니다.",
          "detail": "상품의 재고가 부족합니다."
        }
    """;

    public static final String INVALID_COUPON = """
        {
          "code": 409,
          "message": "비즈니스 정책을 위반한 요청입니다.",
          "detail": "사용자가 보유한 쿠폰이 아닙니다."
        }
    """;

    public static final String EXPIRED_COUPON = """
        {
          "code": 409,
          "message": "비즈니스 정책을 위반한 요청입니다.",
          "detail": "쿠폰이 유효한 기간이 아닙니다."
        }
    """;

    public static final String ALREADY_USED_COUPON = """
        {
          "code": 409,
          "message": "비즈니스 정책을 위반한 요청입니다.",
          "detail": "이미 사용된 쿠폰입니다."
        }
    """;

    public static final String INVALID_ORDER_USER_ID = """
        {
          "code": 400,
          "message": "유효하지 않은 사용자 ID입니다.",
          "detail": "userId는 양의 정수여야 합니다."
        }
    """;

    public static final String INVALID_ORDER_ITEM = """
        {
          "code": 400,
          "message": "상품 수량은 1개 이상이어야 합니다.",
          "detail": "quantity: 0"
        }
    """;

    private OrderSwaggerDocs() {}
}
