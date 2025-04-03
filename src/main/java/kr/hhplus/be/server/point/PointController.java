package kr.hhplus.be.server.point;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import kr.hhplus.be.server.point.docs.PointSwaggerDocs;
import kr.hhplus.be.server.point.dto.PointChargeRequest;
import kr.hhplus.be.server.point.dto.PointChargeResponse;
import kr.hhplus.be.server.point.dto.PointUseRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

import static kr.hhplus.be.server.point.docs.PointSwaggerDocs.*;

@RestController
@RequestMapping("/api/v1/points")
@Validated
public class PointController {

    //포인트 충전
    @Operation(
            summary = "포인트 충전",
            description = "사용자의 포인트를 충전합니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "정상 처리",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(name = "충전 성공 예시", value = CHARGE_SUCCESS)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "유효하지 않은 사용자 ID 또는 충전 금액이 0 이하",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(name = "잘못된 사용자 ID", value = INVALID_USER_ID)
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "1회 최대 충전 금액 초과",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(name = "충전 금액 초과", value = CHARGE_OVER_LIMIT)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @PostMapping("/charge")
    public ResponseEntity<Map<String, Object>> mockCharge(@RequestBody @Valid PointChargeRequest request) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("code", 200);
        response.put("message", "요청이 정상적으로 처리되었습니다.");
        response.put("data", new PointChargeResponse(request.userId(), 1000000L));

        return ResponseEntity.ok(response);
    }
    //포인트 조회
    @Operation(summary = "포인트 조회", description = "사용자의 포인트 잔액을 조회합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(name = "잔액 조회 성공", value = PointSwaggerDocs.POINT_BALANCE_RESPONSE)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 사용자 ID"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @GetMapping
    public ResponseEntity<Map<String, Object>> mockGetBalance(@Parameter(description = "사용자 ID", example = "1")@RequestParam @Min(1) Long userId) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("code", 200);
        response.put("message", "요청이 정상적으로 처리되었습니다.");
        response.put("data", new PointChargeResponse(userId, 1000000L));  // ✅ 재사용

        return ResponseEntity.ok(response);
    }

    //포인트 결제
    @Operation(summary = "결제 처리", description = "주문에 대해 포인트 결제를 수행합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "결제 성공 (No Content)"),
            @ApiResponse(
                    responseCode = "400",
                    description = "유효하지 않은 주문 ID",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(name = "잘못된 주문 ID", value = PointSwaggerDocs.INVALID_ORDER_ID)
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "잔액 부족",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(name = "잔액 부족", value = PointSwaggerDocs.USE_INSUFFICIENT_BALANCE)
                    )
            ),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @PostMapping("/use")
    public ResponseEntity<Void> mockUsePoint(@RequestBody @Valid PointUseRequest request) {
        // 실제로는 포인트 차감 로직이 들어가야 하지만, 여기선 Mock 처리만
        return ResponseEntity.noContent().build(); // 204
    }
}
