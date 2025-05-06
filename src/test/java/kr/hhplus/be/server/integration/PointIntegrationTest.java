package kr.hhplus.be.server.integration;

import kr.hhplus.be.server.application.point.PointCommand;
import kr.hhplus.be.server.application.point.PointFacade;
import kr.hhplus.be.server.application.point.PointResult;
import kr.hhplus.be.server.domain.point.Point;
import kr.hhplus.be.server.domain.point.PointRepository;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserRepository;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;
import static org.instancio.Select.field;

@SpringBootTest
@Transactional
@Rollback
@ActiveProfiles("test")
class PointIntegrationTest {

    @Autowired
    private PointFacade pointFacade;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PointRepository pointRepository;

    private User user ;

    @BeforeEach
    void setup() {
        user = userRepository.save(Instancio.of(User.class)
                .supply(field(User::getId), () -> null)
                .create());
        pointRepository.savePoint(Point.of(user.getId(), BigDecimal.ZERO));
    }

    @Test
    @DisplayName("포인트 충전 성공")
    void testChargePointSuccess() {
        // Arrange
        BigDecimal chargeAmount = new BigDecimal("50000");
        PointCommand.IncreaseRequest request = new PointCommand.IncreaseRequest(user.getId(), chargeAmount);

        // Act
        PointResult.Charge result = pointFacade.charge(request);
        PointResult.UserPoint balance = pointFacade.getUserPoint(new PointCommand.UserIdRequest(user.getId()));

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getBalance()).isEqualByComparingTo(chargeAmount);
        assertThat(balance.getBalance()).isEqualByComparingTo(chargeAmount);
    }

    @Test
    @DisplayName("유효하지 않은 사용자 ID 예외")
    void testChargePointInvalidUser() {
        // Arrange
        Long invalidUserId = 999999L;
        BigDecimal chargeAmount = new BigDecimal("10000");
        PointCommand.IncreaseRequest request = new PointCommand.IncreaseRequest(invalidUserId, chargeAmount);

        // Act & Assert
        assertThatThrownBy(() -> pointFacade.charge(request))
                .isInstanceOf(RuntimeException.class); // 커스텀 예외 클래스로 변경 가능
    }

    @Test
    @DisplayName("1회 최대 충전 금액 초과 예외")
    void testChargeOverMaxSingleAmount() {
        // Arrange
        BigDecimal overAmount = new BigDecimal("1000001");
        PointCommand.IncreaseRequest request = new PointCommand.IncreaseRequest(user.getId(), overAmount);

        // Act & Assert
        assertThatThrownBy(() -> pointFacade.charge(request))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("누적 최대 충전 금액 초과 예외")
    void testChargeOverTotalLimit() {
        // Arrange
        BigDecimal oneCharge = new BigDecimal("1000000");
        for (int i = 0; i < 5; i++) {
            pointFacade.charge(new PointCommand.IncreaseRequest(user.getId(), oneCharge));
        }
        BigDecimal extra = new BigDecimal("1");
        PointCommand.IncreaseRequest sixth = new PointCommand.IncreaseRequest(user.getId(), extra);

        // Act & Assert
        assertThatThrownBy(() -> pointFacade.charge(sixth))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("잔액 조회 성공")
    void testGetUserPoint() {
        // Act
        PointResult.UserPoint result = pointFacade.getUserPoint(new PointCommand.UserIdRequest(user.getId()));

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getBalance()).isNotNull();
    }

    @Test
    @DisplayName("포인트 이력 조회")
    void testGetPointHistory() {
        // Arrange
        BigDecimal chargeAmount = new BigDecimal("50000");
        pointFacade.charge(new PointCommand.IncreaseRequest(user.getId(), chargeAmount));

        // Act
        var result = pointFacade.getPointHistory(new PointCommand.UserIdRequest(user.getId()));

        // Assert
        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getAmount()).isEqualByComparingTo(chargeAmount);
    }
}
