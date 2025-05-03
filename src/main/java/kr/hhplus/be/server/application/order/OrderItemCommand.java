package kr.hhplus.be.server.application.order;

//상품 단위 요청
public record OrderItemCommand (
        Long productId,
        Long quantity
){}
