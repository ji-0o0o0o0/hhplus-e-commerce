package kr.hhplus.be.server.application.product;

import kr.hhplus.be.server.application.order.CreateOrderCommand;
import kr.hhplus.be.server.application.order.CreateOrderResult;

import java.util.List;

public interface ProductUseCase {
    List<ProductResult> getAllProducts();
}