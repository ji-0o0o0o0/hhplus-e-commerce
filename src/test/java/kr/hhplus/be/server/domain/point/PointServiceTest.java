package kr.hhplus.be.server.domain.point;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PointServiceTest {

    private PointRepository repository;
    private PointService pointService;

    @BeforeEach
    void setUp() {
        repository = mock(PointRepository.class);
        pointService = new PointService(repository);
    }

    @Test
    @DisplayName("기존 포인트가 존재하면 반환")
    void getOrCreatePoint_existing_returnsPoint() {
        // Arrange
        Long userId = 1L;
        Point existing = new Point(userId);
        when(repository.findByUserId(userId)).thenReturn(Optional.of(existing));

        // Act
        Point result = pointService.getOrCreatePoint(userId);

        // Assert
        assertEquals(existing, result);
    }

    @Test
    @DisplayName("포인트 객체가 없으면 새로 생성")
    void getOrCreatePoint_newInstanceCreated() {
        // Arrange
        Long userId = 2L;
        when(repository.findByUserId(userId)).thenReturn(Optional.empty());

        // Act
        Point result = pointService.getOrCreatePoint(userId);

        // Assert
        assertNotNull(result);
        assertEquals(userId, result.getUserId());
        assertEquals(0, result.getBalance());
    }

    @Test
    @DisplayName("포인트 저장이 호출되면 repository에 위임")
    void save_delegatesToRepository() {
        // Arrange
        Point point = new Point(1L);

        // Act
        pointService.save(point);

        // Assert
        verify(repository).save(point);
    }

    @Test
    @DisplayName("포인트 이력 저장이 repository에 위임")
    void saveHistory_delegatesToRepository() {
        // Arrange
        Point point = new Point(1L);
        int amount = 10000;

        // Act
        pointService.saveHistory(point, amount,PointUseStatus.CHARGE);

        // Assert
        verify(repository).saveHistory(any(PointHistory.class));
    }
}
