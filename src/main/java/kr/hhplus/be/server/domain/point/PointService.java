package kr.hhplus.be.server.domain.point;

import kr.hhplus.be.server.common.exception.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static kr.hhplus.be.server.common.exception.ErrorCode.*;

@RequiredArgsConstructor
@Service
public class PointService {

    private final PointRepository pointRepository;

    /**
     * 사용자 포인트 조회
     * @param userId
     * @return 포인트 잔액 정보
     * @exception ApiException 등록되지 않은 유저일 경우 예외 발생
     */
    public Point findPointByUserId(long userId) {
        return pointRepository.findPointByUserId(userId)
                .orElseThrow(() -> new ApiException(INVALID_USER));
    }

    public PointInfo.Balance getPointBalance(long userId) {
        return PointInfo.Balance.from(findPointByUserId(userId));
    }

    //포인트 충전
    public PointInfo.Increase increase(long userId,BigDecimal amount){
        //0. 사용자 포인트 조회
        Point point =findPointByUserId(userId);
       //1. 도메인에서  유효성 검증 및 충전
        point.increase(amount);
        //2. 포인트 변화 저장
        pointRepository.savePoint(point);
        //3, 이력 저장
        pointRepository.savePointHistory(PointHistory.saveHistory(point,amount,PointUseStatus.CHARGE));

        return PointInfo.Increase.from(point);
    }
    //포인트 차감
    public PointInfo.Decrease decrease(long userId, BigDecimal amount){
        //0. 사용자 포인트 조회
        Point point =findPointByUserId(userId);
        //1. 도메인에서  유효성 검증 및 사용
        point.decrease(amount);
        //2. 포인트 변화 저장
        pointRepository.savePoint(point);
        //3, 이력 저장
        pointRepository.savePointHistory(PointHistory.saveHistory(point,amount,PointUseStatus.USE));

        return PointInfo.Decrease.from(point);
    }
    //사용자의 포인트 사용/충전 이력 조회
    public List<PointInfo.History> getPonintHistories(long userId){
        List<PointHistory> histories = pointRepository.findPointHistoryByUserId(userId);

        return histories.stream()
                .map(PointInfo.History::from)
                .collect(Collectors.toList());
    }
}