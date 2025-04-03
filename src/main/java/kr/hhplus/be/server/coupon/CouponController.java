package kr.hhplus.be.server.coupon;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import kr.hhplus.be.server.coupon.dto.CouponIssueRequest;
import kr.hhplus.be.server.coupon.dto.CouponListResponse;
import kr.hhplus.be.server.coupon.dto.CouponResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static kr.hhplus.be.server.coupon.docs.CouponSwaggerDocs.*;

@RestController
@RequestMapping("/api/v1/coupons")
@Valid
public class CouponController {

    //선착순 쿠폰 발급
    @Operation(summary = "선착순 쿠폰 발급", description = "사용자가 선착순으로 쿠폰을 발급받습니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "쿠폰 발급 성공",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(name = "발급 성공", value = ISSUE_SUCCESS))),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 쿠폰 ID",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(name = "잘못된 쿠폰 ID", value = INVALID_COUPON_ID))),
            @ApiResponse(responseCode = "404", description = "쿠폰 없음",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(name = "쿠폰 없음", value = COUPON_NOT_FOUND))),
            @ApiResponse(responseCode = "409", description = "잔여 수량 없음 / 중복 발급",
                    content = @Content(mediaType = "application/json",
                            examples = {
                                    @ExampleObject(name = "잔여 수량 없음", value = NO_MORE_COUPONS),
                                    @ExampleObject(name = "이미 발급받음", value = ALREADY_ISSUED)
                            }
                    )
            )
    })
    @PostMapping("/{couponId}/issue")
    public ResponseEntity<Map<String, Object>> mockIssueCoupon(
            @PathVariable Long couponId,
            @RequestBody @Valid CouponIssueRequest request) {

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("code", 201);
        response.put("status", "Created");
        response.put("message", "요청이 정상적으로 처리되었습니다.");
        response.put("data", Map.of());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    //보유쿠폰 조회
    @Operation(summary = "보유 쿠폰 조회", description = "사용자의 보유 쿠폰 목록을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(name = "조회 성공 예시", value = COUPON_LIST_SUCCESS))),
            @ApiResponse(responseCode = "400", description = "잘못된 사용자 ID",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(name = "잘못된 userId", value = INVALID_USER_ID)))
    })
    @GetMapping
    public ResponseEntity<Map<String, Object>> mockGetUserCoupons(@Parameter(description = "사용자 ID", example = "1")@RequestParam @Min(1) Long userId) {
        List<CouponResponse> coupons = List.of(
                new CouponResponse(1L, "10% 할인 쿠폰", "RATE", 10, LocalDate.of(2025, 8, 1), LocalDate.of(2025, 8, 31)),
                new CouponResponse(2L, "10,000원 할인 쿠폰", "AMOUNT", 10000, LocalDate.of(2025, 8, 1), LocalDate.of(2025, 8, 31))
        );

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("code", 200);
        response.put("message", "요청이 정상적으로 처리되었습니다.");
        response.put("data", new CouponListResponse(userId, coupons));

        return ResponseEntity.ok(response);
    }
}
