package kr.hhplus.be.server.infrastructure.point;

import kr.hhplus.be.server.domain.point.PointHistory;
import kr.hhplus.be.server.domain.point.Point;
import kr.hhplus.be.server.domain.point.PointRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class InMemoryPointRepository implements PointRepository {

    @Override
    public Optional<Point> findPointByUserId(Long userId) {
        return Optional.empty();
    }

    @Override
    public List<PointHistory> findPointHistoryByUserId(Long userId) {
        return null;
    }

    @Override
    public Point savePoint(Point point) {
        return null;
    }

    @Override
    public PointHistory savePointHistory(PointHistory pointHistory) {
        return null;
    }
}
