package kr.hhplus.be.server.application.point;


import kr.hhplus.be.server.domain.pay.PayRepository;
import kr.hhplus.be.server.domain.pay.PayService;
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
    public PayService payService(PayRepository payRepository) {
        return new PayService(payRepository);
    }
}
