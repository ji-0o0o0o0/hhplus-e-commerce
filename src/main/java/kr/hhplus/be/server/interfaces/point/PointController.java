package kr.hhplus.be.server.interfaces.point;

import kr.hhplus.be.server.application.point.PointFacade;
import kr.hhplus.be.server.application.point.PointResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/points")
public class PointController {

    private final PointFacade pointFacade;


    @PostMapping("/charge")
    public ResponseEntity<PointResponse> charge(@RequestBody PointRequest request) {
        PointResult result = pointFacade.charge(request.toCommand());
        return ResponseEntity.ok(PointResponse.from(result));
    }
}