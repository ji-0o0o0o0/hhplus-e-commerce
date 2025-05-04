package kr.hhplus.be.server.infrastructure.point;

import kr.hhplus.be.server.domain.point.PointHistory;
import kr.hhplus.be.server.domain.point.Point;
import kr.hhplus.be.server.domain.point.PointRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class InMemoryPointRepository implements PointRepository {
    private final Map<Long, Point> pointStore = new HashMap<>();
    private final List<PointHistory> histories = new ArrayList<>();

    @Override
    public Optional<Point> findByUserId(Long userId) {
        return Optional.ofNullable(pointStore.get(userId));
    }

    @Override
    public void save(Point point) {
        pointStore.put(point.getUserId(), point);
    }

    @Override
    public void saveHistory(PointHistory history) {
        histories.add(history);
    }
}
