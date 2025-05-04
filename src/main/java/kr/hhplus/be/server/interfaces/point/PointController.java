package kr.hhplus.be.server.interfaces.point;

import kr.hhplus.be.server.application.point.PointCommand;
import kr.hhplus.be.server.application.point.PointFacade;
import kr.hhplus.be.server.application.point.PointResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/points")
public class PointController {

    private final PointFacade pointFacade;


    //포인트 조회
    @GetMapping("/{userId}")
    public PointResponse.UserPoint  getUserPoint(@PathVariable long userId){
        PointResult.UserPoint userPoint = pointFacade.getUserPoint(new PointCommand.UserIdRequest(userId));
        return PointResponse.UserPoint.from(userPoint);
    }
    //포인트 충전
    @PostMapping("/charge")
    public ResponseEntity <PointResponse.Charge> chargePoint(@RequestBody PointRequest.Charge request){
        PointResult.Charge charge = pointFacade.charge(new PointCommand.IncreaseRequest(request.userId(), request.amount()));
        return ResponseEntity.ok(PointResponse.Charge.from(charge));
    }
    //포인트 내역 조회
    // 포인트 내역 조회
    @GetMapping("/histories/{userId}")
    public List<PointResponse.History> findPointHistories(
            @PathVariable Long userId
    ) {
        List<PointResult.History> histories = pointFacade.getPointHistory(new PointCommand.UserIdRequest(userId));

        return histories.stream()
                .map(PointResponse.History::from)
                .collect(Collectors.toList());

    }
}