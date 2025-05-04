package kr.hhplus.be.server.application.point;

import kr.hhplus.be.server.domain.point.PointInfo;
import kr.hhplus.be.server.domain.point.PointUseStatus;
import kr.hhplus.be.server.domain.point.Point;
import kr.hhplus.be.server.domain.point.PointService;
import kr.hhplus.be.server.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class PointFacade {

    private final PointService pointService;

    //포인트 조회
    public PointResult.UserPoint getUserPoint(PointCommand.UserIdRequest command){
        PointInfo.Balance pointBalance = pointService.getPointBalance(command.userId());
        return PointResult.UserPoint.from(pointBalance);
    }
    //포인트 충전
    public PointResult.Charge charge(PointCommand.IncreaseRequest command){
       PointInfo.Increase charge= pointService.increase(command.userId(),command.amount());
       return PointResult.Charge.from(charge);
    }

    //포인트 이력 조회
    public List<PointResult.History> getPointHistory(PointCommand.UserIdRequest command){
        List<PointInfo.History> pointHistory = pointService.getPointHistories(command.userId());

        return pointHistory.stream()
                .map(PointResult.History::from)
                .collect(Collectors.toList());
    }
}

