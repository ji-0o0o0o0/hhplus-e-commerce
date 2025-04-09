package kr.hhplus.be.server.application.order;

import kr.hhplus.be.server.domain.order.OrderRepository;
import kr.hhplus.be.server.domain.order.OrderService;
import kr.hhplus.be.server.domain.product.ProductRepository;
import kr.hhplus.be.server.domain.product.ProductService;
import kr.hhplus.be.server.domain.order.CouponValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrderConfig {
    @Bean
    public OrderUseCase orderUseCase(
            ProductService productService,
            CouponValidator couponValidator,
            OrderRepository orderRepository
    ) {
        OrderService orderService = new OrderService(orderRepository);
        return new OrderFacade(productService, couponValidator, orderService);
    }
    @Bean
    public CouponValidator couponValidator() {
        return new CouponValidatorImpl(); // 직접 생성
    }

    @Bean
    public ProductService productService(ProductRepository productRepository) {
        return new ProductService(productRepository);
    }
}
