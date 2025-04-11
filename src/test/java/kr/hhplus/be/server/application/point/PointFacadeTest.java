package kr.hhplus.be.server.application.point;

import kr.hhplus.be.server.application.point.PointCommand;
import kr.hhplus.be.server.application.point.PointFacade;
import kr.hhplus.be.server.application.point.PointResult;
import kr.hhplus.be.server.domain.point.PointUseStatus;
import kr.hhplus.be.server.domain.point.UserPoint;
import kr.hhplus.be.server.domain.point.UserPointService;
import kr.hhplus.be.server.domain.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


  class PointFacadeTest {

    private UserService userService;
    private UserPointService userPointService;
    private PointFacade pointFacade;

    @BeforeEach
    void setUp() {
        userService = mock(UserService.class);
        userPointService = mock(UserPointService.class);

        pointFacade = new PointFacade(userService, userPointService);
    }

    @Test
    @DisplayName("정상 포인트 충전 시 잔액 반환")
    void charge_success() {
        // Arrange
        Long userId = 1L;
        int chargeAmount = 100000;
        UserPoint point = new UserPoint(userId);
        when(userPointService.getOrCreatePoint(userId)).thenReturn(point);

        // Act
        PointResult result = pointFacade.charge(new PointCommand(userId, chargeAmount));

        // Assert
        assertEquals(userId, result.userId());
        assertEquals(chargeAmount, result.balance());
        verify(userService).getUserById(userId);
        verify(userPointService).save(point);
        verify(userPointService).saveHistory(point, chargeAmount, PointUseStatus.CHARGE);
    }

    @Test
    @DisplayName("1회 충전 금액이 초과되면 예외 발생")
    void charge_exceedsSingleLimit_throwsException() {
        // Arrange
        //1회충전 최대금액 100만원
        Long userId = 1L;
        int overAmount = 2_000_000;
        UserPoint point = new UserPoint(userId);
        when(userPointService.getOrCreatePoint(userId)).thenReturn(point);

        // Act & Assert
        PointCommand command = new PointCommand(userId, overAmount);

        assertThrows(IllegalArgumentException.class, () -> pointFacade.charge(command));
        verify(userService).getUserById(userId);
    }

    @Test
    @DisplayName("누적 충전 금액이 초과되면 예외 발생")
    void charge_exceedsTotalLimit_throwsException() {
        // Arrange
        //누적 최대 금액 500만원
        Long userId = 1L;
        int chargeAmount = 1_000_000;
        UserPoint point = new UserPoint(userId);
        when(userPointService.getOrCreatePoint(userId)).thenReturn(point);

        // Act
        //
        for (int i = 0; i < 5; i++) {
            pointFacade.charge(new PointCommand(userId, chargeAmount));
        }

        // Assert
        PointCommand lastCommand = new PointCommand(userId, 1);
        assertThrows(IllegalArgumentException.class, () -> pointFacade.charge(lastCommand));
        verify(userService, times(6)).getUserById(userId);
    }

    @Test
    @DisplayName("존재하지 않는 사용자 ID로 충전 시 예외 발생")
    void charge_invalidUser_throwsException() {
        // Arrange
        Long userId = 999L;
        int chargeAmount = 100_000;
        when(userService.getUserById(userId)).thenThrow(new IllegalArgumentException("존재하지 않는 사용자"));

        // Act & Assert
        PointCommand command = new PointCommand(userId, chargeAmount);
        assertThrows(IllegalArgumentException.class, () -> pointFacade.charge(command));
        verify(userService).getUserById(userId);
        verifyNoInteractions(userPointService);
    }
}

