package kr.hhplus.be.server.application.point;


import kr.hhplus.be.server.domain.point.UserPointRepository;
import kr.hhplus.be.server.domain.point.UserPointService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PointConfig {

    @Bean
    public UserPointService userPointService(UserPointRepository userPointRepository) {
        return new UserPointService(userPointRepository);
    }
}
