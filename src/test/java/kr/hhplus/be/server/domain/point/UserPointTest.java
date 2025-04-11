package kr.hhplus.be.server.domain.point;

import kr.hhplus.be.server.domain.point.UserPoint;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserPointTest {

    @Test
    @DisplayName("정상 충전 시 잔액과 누적 충전액 증가")
    void charge_success_increasesBalanceAndTotal() {
        // Arrange
        UserPoint point = new UserPoint(1L);
        int chargeAmount = 100_000;

        // Act
        point.charge(chargeAmount);

        // Assert
        assertEquals(100_000, point.getBalance());
    }

    @Test
    @DisplayName("0원 이하 충전 시 예외 발생")
    void charge_zeroOrNegative_throwsException() {
        // Arrange
        UserPoint point = new UserPoint(1L);

        // Act & Assert
        IllegalArgumentException ex1 = assertThrows(IllegalArgumentException.class, () -> point.charge(0));
        assertEquals("1회 충전 금액은 0보다 크고 1,000,000 이하여야 합니다.", ex1.getMessage());

        IllegalArgumentException ex2 = assertThrows(IllegalArgumentException.class, () -> point.charge(-500));
        assertEquals("1회 충전 금액은 0보다 크고 1,000,000 이하여야 합니다.", ex2.getMessage());
    }

    @Test
    @DisplayName("1회 최대 금액(1,000,000원) 초과 충전 시 예외 발생")
    void charge_exceedsMaxOnce_throwsException() {
        // Arrange
        UserPoint point = new UserPoint(1L);

        // Act & Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> point.charge(1_000_001));
        assertEquals("1회 충전 금액은 0보다 크고 1,000,000 이하여야 합니다.", ex.getMessage());
    }

    @Test
    @DisplayName("누적 충전 한도(5,000,000) 초과 시 예외 발생")
    void charge_exceedsMaxTotal_throwsException() {
        // Arrange
        //500만원 충전
        UserPoint point = new UserPoint(1L);
        for (int i = 0; i < 5; i++) {
            point.charge(1_000_000);
        }

        // Act & Assert
        //1원이라도 추가하면 에러발생
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> point.charge(1));
        assertEquals("누적 충전 금액은 5,000,000원을 초과할 수 없습니다.", ex.getMessage());
    }
}
