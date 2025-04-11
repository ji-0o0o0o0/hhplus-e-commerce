package kr.hhplus.be.server.application.point;

import kr.hhplus.be.server.domain.point.PointUseStatus;
import kr.hhplus.be.server.domain.point.Point;
import kr.hhplus.be.server.domain.point.PointService;
import kr.hhplus.be.server.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PointFacade {

    private final UserService userService;
    private final PointService pointService;

    public PointResult charge(PointCommand command) {
        Long userId = command.userId();
        int amount = command.chargeAmount();

        //사용자 검증
        userService.getUserById(userId);
        //포인트 조회
        Point point = pointService.getOrCreatePoint(userId);
        point.charge(amount);
        //포인트 충전
        pointService.save(point);
        //포인트 이력 업데이트
        pointService.saveHistory(point, amount, PointUseStatus.USE);
        return new PointResult(userId, point.getBalance());
    }
}

