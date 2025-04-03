package kr.hhplus.be.server.point.docs;


public final class PointSwaggerDocs {

    // ✅ 포인트 충전 성공
    public static final String CHARGE_SUCCESS = """
        {
          "code": 200,
          "message": "요청이 정상적으로 처리되었습니다.",
          "data": {
            "userId": 1,
            "balance": 1000000
          }
        }
    """;

    // ✅ 충전 초과
    public static final String CHARGE_OVER_LIMIT = """
        {
          "code": 409,
          "message": "1회 충전 금액은 1,000,000원을 초과할 수 없습니다.",
          "detail": "입력값: 1,500,000원"
        }
    """;

    // ✅ 유효하지 않은 사용자 ID (충전/조회 모두 사용 가능)
    public static final String INVALID_USER_ID = """
        {
          "code": 400,
          "message": "유효하지 않은 사용자 ID입니다.",
          "detail": "userId는 양의 정수여야 합니다."
        }
    """;

    // ✅ 결제 실패 - 잔액 부족
    public static final String USE_INSUFFICIENT_BALANCE = """
        {
          "code": 409,
          "message": "포인트 잔액이 부족합니다.",
          "detail": "현재 잔액: 10,000원, 결제 금액: 50,000원"
        }
    """;

    // ✅ 결제 실패 - 주문 ID 잘못됨
    public static final String INVALID_ORDER_ID = """
        {
          "code": 400,
          "message": "유효하지 않은 주문 ID입니다.",
          "detail": "orderId는 양의 정수여야 합니다."
        }
    """;

    // ✅ 포인트 조회 성공
    public static final String POINT_BALANCE_RESPONSE = """
        {
          "code": 200,
          "message": "요청이 정상적으로 처리되었습니다.",
          "data": {
            "userId": 1,
            "balance": 150000
          }
        }
    """;

    private PointSwaggerDocs() {}
}
