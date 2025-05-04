package kr.hhplus.be.server.domain.point;

import kr.hhplus.be.server.common.exception.ApiException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Point 도메인 단위 테스트")
class PointTest {

    @Test
    @DisplayName("정상 충전 시 잔액 증가 및 히스토리 생성")
    void charge_success() {
        Point point = new Point(1L, new BigDecimal("10000"));

        PointHistory history = point.charge(new BigDecimal("5000"));

        assertEquals(new BigDecimal("15000"), point.getBalance());
        assertEquals(PointUseStatus.CHARGE, history.getType());
        assertEquals(new BigDecimal("5000"), history.getAmount());
    }

    @Test
    @DisplayName("음수 금액 충전 시 예외")
    void charge_negativeAmount_throwsException() {
        Point point = new Point(1L, new BigDecimal("10000"));

        assertThrows(ApiException.class, () -> point.charge(new BigDecimal("-1")));
    }

    @Test
    @DisplayName("1회 최대 충전 금액 초과 시 예외")
    void charge_exceedOnceLimit_throwsException() {
        Point point = new Point(1L, new BigDecimal("0"));

        assertThrows(ApiException.class, () -> point.charge(new BigDecimal("1000001")));
    }

    @Test
    @DisplayName("누적 충전 한도 초과 시 예외")
    void charge_exceedTotalLimit_throwsException() {
        Point point = new Point(1L, new BigDecimal("5000000"));

        assertThrows(ApiException.class, () -> point.charge(new BigDecimal("1")));
    }

    @Test
    @DisplayName("정상 차감 시 잔액 감소 및 히스토리 생성")
    void use_success() {
        Point point = new Point(1L, new BigDecimal("10000"));

        PointHistory history = point.use(new BigDecimal("3000"));

        assertEquals(new BigDecimal("7000"), point.getBalance());
        assertEquals(PointUseStatus.USE, history.getType());
        assertEquals(new BigDecimal("3000"), history.getAmount());
    }

    @Test
    @DisplayName("음수 금액 차감 시 예외")
    void use_negativeAmount_throwsException() {
        Point point = new Point(1L, new BigDecimal("10000"));

        assertThrows(ApiException.class, () -> point.use(new BigDecimal("-500")));
    }

    @Test
    @DisplayName("잔액 부족 시 예외 발생")
    void use_insufficientBalance_throwsException() {
        Point point = new Point(1L, new BigDecimal("1000"));

        assertThrows(ApiException.class, () -> point.use(new BigDecimal("2000")));
    }

    @Test
    @DisplayName("1회 차감 한도 초과 시 예외 발생")
    void use_exceedsLimit_throwsException() {
        Point point = new Point(1L, new BigDecimal("10000000")); // 충분한 잔액

        assertThrows(ApiException.class, () -> point.use(new BigDecimal("5000001")));
    }
}
