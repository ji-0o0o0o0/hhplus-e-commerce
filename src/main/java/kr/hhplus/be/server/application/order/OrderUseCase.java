package kr.hhplus.be.server.application.order;

public interface OrderUseCase {
    CreateOrderResult createOrder(CreateOrderCommand command);
}
