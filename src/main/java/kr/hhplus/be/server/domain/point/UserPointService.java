package kr.hhplus.be.server.domain.point;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserPointService {
    private final UserPointRepository repository;

    //포인트를 가져옴(포인트 테이블에 없는 경우 생성 후 리턴)
    public UserPoint getOrCreatePoint(Long userId) {
        return repository.findByUserId(userId).orElseGet(() -> new UserPoint(userId));
    }

    //포인트 저장
    public void save(UserPoint userPoint) {
        repository.save(userPoint);
    }

    //포인트 이력 저장
    public void saveHistory(UserPoint point, int amount,PointUseStatus type) {
        repository.saveHistory(new PointHistory(point.getPointId(),amount,point.getBalance(),type));
    }
}