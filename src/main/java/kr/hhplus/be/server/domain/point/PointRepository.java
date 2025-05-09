package kr.hhplus.be.server.domain.point;

import java.util.List;
import java.util.Optional;

public interface PointRepository {
    Optional<Point> findPointByUserId(Long userId);

    List<PointHistory> findPointHistoryByUserId(Long userId);

    void savePoint(Point point);

    void savePointHistory(PointHistory pointHistory);

    void deleteAll();
}