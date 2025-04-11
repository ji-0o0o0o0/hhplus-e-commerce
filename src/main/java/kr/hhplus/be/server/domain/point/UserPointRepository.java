package kr.hhplus.be.server.domain.point;

import java.util.Optional;

public interface UserPointRepository {
    Optional<UserPoint> findByUserId(Long userId);
    void save(UserPoint point);
    void saveHistory(PointHistory history);
}