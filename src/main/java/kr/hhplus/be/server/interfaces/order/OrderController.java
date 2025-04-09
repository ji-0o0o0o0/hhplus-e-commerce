package kr.hhplus.be.server.interfaces.order;

import kr.hhplus.be.server.application.order.CreateOrderCommand;
import kr.hhplus.be.server.application.order.CreateOrderResult;
import kr.hhplus.be.server.application.order.OrderUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderUseCase orderUseCase;

    @PostMapping
    public ResponseEntity<OrderResponse> create(@RequestBody OrderRequest request) {
        CreateOrderCommand command = request.toCommand();
        CreateOrderResult result = orderUseCase.createOrder(command);
        return ResponseEntity.ok(OrderResponse.from(result));
    }
}