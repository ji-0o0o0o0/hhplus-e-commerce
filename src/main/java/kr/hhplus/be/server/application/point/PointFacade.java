package kr.hhplus.be.server.application.point;

import kr.hhplus.be.server.domain.point.PointUseStatus;
import kr.hhplus.be.server.domain.point.UserPoint;
import kr.hhplus.be.server.domain.point.UserPointService;
import kr.hhplus.be.server.domain.user.UserService;
import org.springframework.stereotype.Service;

@Service
public class PointFacade {

    private final UserService userService;
    private final UserPointService userPointService;

    public PointFacade(UserService userService, UserPointService userPointService) {
        this.userService = userService;
        this.userPointService = userPointService;
    }

    public PointResult charge(PointCommand command) {
        Long userId = command.userId();
        int amount = command.chargeAmount();

        //사용자 검증
        userService.getUserById(userId);
        //포인트 조회
        UserPoint point = userPointService.getOrCreatePoint(userId);
        point.charge(amount);
        //포인트 충전
        userPointService.save(point);
        //포인트 이력 업데이트
        userPointService.saveHistory(point, amount, PointUseStatus.CHARGE);
        return new PointResult(userId, point.getBalance());
    }
}

