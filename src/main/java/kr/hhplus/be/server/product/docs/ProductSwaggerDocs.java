package kr.hhplus.be.server.product.docs;



public final class ProductSwaggerDocs {

    public static final String PRODUCT_LIST = """
        {
          "code": 200,
          "message": "요청이 정상적으로 처리되었습니다.",
          "data": {
            "products": [
              {
                "id": 1,
                "name": "MacBook Pro",
                "price": 2000000,
                "stock": 10
              },
              {
                "id": 2,
                "name": "iPhone 12",
                "price": 1200000,
                "stock": 5
              }
            ]
          }
        }
    """;

    public static final String BEST_PRODUCTS = """
        {
          "code": 200,
          "message": "요청이 정상적으로 처리되었습니다.",
          "data": [
            {
              "id": 1,
              "name": "Ice Americano",
              "price": 1000,
              "sales": 100,
              "stock": 100
            },
            {
              "id": 2,
              "name": "iPhone 12",
              "price": 1200000,
              "sales": 90,
              "stock": 50
            }
          ]
        }
    """;

    public static final String INTERNAL_ERROR = """
        {
          "code": 500,
          "message": "서버 내부 오류가 발생했습니다.",
          "detail": "예상치 못한 오류가 발생했습니다."
        }
    """;

    private ProductSwaggerDocs() {}
}
