package kr.hhplus.be.server.domain.point;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PointService {

    private final PointRepository repository;

    //포인트를 가져옴(포인트 테이블에 없는 경우 생성 후 리턴)
    public Point getOrCreatePoint(Long userId) {
        return repository.findByUserId(userId).orElseGet(() -> new Point(userId));
    }

    //포인트 저장
    public void save(Point point) {
        repository.save(point);
    }

    //포인트 사용
    public void use(Long userId, int amount) {
        Point userPoint = repository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("포인트 정보 없음"));

        if (userPoint.getBalance() < amount) {
            throw new IllegalStateException("포인트 잔액이 부족합니다.");
        }

        userPoint.use(amount);
        repository.save(userPoint);
    }

    //포인트 이력 저장
    public void saveHistory(Point point, int amount, PointUseStatus type) {
        repository.saveHistory(new PointHistory(point.getPointId(),amount,point.getBalance(),type));
    }

    //잔액 충분한지 검증
    public int getBalance(Long userId) {
        Point point = repository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("포인트 정보 없음"));
        return point.getBalance();
    }
}