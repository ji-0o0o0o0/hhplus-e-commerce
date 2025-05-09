package kr.hhplus.be.server.infrastructure.point;

import kr.hhplus.be.server.domain.point.Point;
import kr.hhplus.be.server.domain.point.PointHistory;
import kr.hhplus.be.server.domain.point.PointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class PointRepositoryImpl implements PointRepository {
    private final PointJpaRepository pointJpaRepository;
    private final PointHistoryJpaRepository pointHistoryJpaRepository;
    @Override
    public Optional<Point> findPointByUserId(Long userId) {
        return pointJpaRepository.findByUserId(userId);
    }

    @Override
    public List<PointHistory> findPointHistoryByUserId(Long userId) {
        return pointHistoryJpaRepository.findAllByUserId(userId);
    }

    @Override
    public void savePoint(Point point) {
        pointJpaRepository.save(point);
    }


    @Override
    public void savePointHistory(PointHistory pointHistory) {
        pointHistoryJpaRepository.save(pointHistory);
    }

    @Override
    public void deleteAll() {
        pointJpaRepository.deleteAll();
    }
}
