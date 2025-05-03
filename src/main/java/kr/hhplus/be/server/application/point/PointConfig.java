package kr.hhplus.be.server.application.point;


import kr.hhplus.be.server.domain.paymant.PaymentRepository;
import kr.hhplus.be.server.domain.paymant.PaymentService;
import kr.hhplus.be.server.domain.point.PointRepository;
import kr.hhplus.be.server.domain.point.PointService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PointConfig {

    @Bean
    public PointService userPointService(PointRepository pointRepository) {
        return new PointService(pointRepository);
    }
    @Bean
    public PaymentService payService(PaymentRepository paymentRepository) {
        return new PaymentService(paymentRepository);
    }
}
