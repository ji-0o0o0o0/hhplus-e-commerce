package kr.hhplus.be.server.infrastructure.point;

import kr.hhplus.be.server.domain.point.PointHistory;
import kr.hhplus.be.server.domain.point.UserPoint;
import kr.hhplus.be.server.domain.point.UserPointRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class InMemoryPointRepository implements UserPointRepository {
    private final Map<Long, UserPoint> pointStore = new HashMap<>();
    private final List<PointHistory> histories = new ArrayList<>();

    @Override
    public Optional<UserPoint> findByUserId(Long userId) {
        return Optional.ofNullable(pointStore.get(userId));
    }

    @Override
    public void save(UserPoint point) {
        pointStore.put(point.getUserId(), point);
    }

    @Override
    public void saveHistory(PointHistory history) {
        histories.add(history);
    }
}
