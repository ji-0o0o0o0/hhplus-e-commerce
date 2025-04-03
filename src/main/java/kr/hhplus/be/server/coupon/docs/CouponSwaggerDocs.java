package kr.hhplus.be.server.coupon.docs;

public final class CouponSwaggerDocs {

    // ✅ 발급 성공
    public static final String ISSUE_SUCCESS = """
        {
          "code": 201,
          "status": "Created",
          "message": "요청이 정상적으로 처리되었습니다.",
          "data": {}
        }
    """;

    public static final String NO_MORE_COUPONS = """
        {
          "code": 409,
          "message": "비즈니스 정책을 위반한 요청입니다.",
          "detail": "쿠폰의 잔여 수량이 부족합니다."
        }
    """;

    public static final String ALREADY_ISSUED = """
        {
          "code": 409,
          "message": "비즈니스 정책을 위반한 요청입니다.",
          "detail": "이미 쿠폰을 발급 받았습니다."
        }
    """;

    public static final String INVALID_COUPON_ID = """
        {
          "code": 400,
          "message": "유효하지 않은 쿠폰 ID입니다.",
          "detail": "couponId는 양의 정수여야 합니다."
        }
    """;

    public static final String COUPON_NOT_FOUND = """
        {
          "code": 404,
          "message": "쿠폰을 찾을 수 없습니다.",
          "detail": "존재하지 않는 쿠폰 ID입니다."
        }
    """;

    public static final String COUPON_LIST_SUCCESS = """
        {
          "code": 200,
          "message": "요청이 정상적으로 처리되었습니다.",
          "data": {
            "userId": 1,
            "coupons": [
              {
                "id": 1,
                "title": "10% 할인 쿠폰",
                "discountType": "RATE",
                "discountValue": 10,
                "startDate": "2025-08-01",
                "endDate": "2025-08-31"
              },
              {
                "id": 2,
                "title": "10,000원 할인 쿠폰",
                "discountType": "AMOUNT",
                "discountValue": 10000,
                "startDate": "2025-08-01",
                "endDate": "2025-08-31"
              }
            ]
          }
        }
    """;

    public static final String INVALID_USER_ID = """
        {
          "code": 400,
          "message": "유효하지 않은 사용자 ID입니다.",
          "detail": "userId는 양의 정수여야 합니다."
        }
    """;

    private CouponSwaggerDocs() {}
}
