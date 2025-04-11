package kr.hhplus.be.server.lagacy.order;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import kr.hhplus.be.server.lagacy.order.docs.OrderSwaggerDocs;
import kr.hhplus.be.server.lagacy.order.dto.OrderCreateResponse;
import kr.hhplus.be.server.lagacy.order.dto.OrderCreateRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    //주문 생성
    @Operation(summary = "주문 생성", description = "상품을 주문하고 결제 대기 상태로 저장합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "주문 성공",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(name = "주문 성공", value = OrderSwaggerDocs.ORDER_SUCCESS))),
            @ApiResponse(responseCode = "400", description = "잘못된 입력값",
                    content = @Content(mediaType = "application/json",
                            examples = {
                                    @ExampleObject(name = "잘못된 userId", value = OrderSwaggerDocs.INVALID_ORDER_USER_ID),
                                    @ExampleObject(name = "상품 수량 0", value = OrderSwaggerDocs.INVALID_ORDER_ITEM)
                            })),
            @ApiResponse(responseCode = "409", description = "쿠폰/재고 이슈",
                    content = @Content(mediaType = "application/json",
                            examples = {
                                    @ExampleObject(name = "재고 부족", value = OrderSwaggerDocs.OUT_OF_STOCK),
                                    @ExampleObject(name = "쿠폰 아님", value = OrderSwaggerDocs.INVALID_COUPON),
                                    @ExampleObject(name = "쿠폰 만료", value = OrderSwaggerDocs.EXPIRED_COUPON),
                                    @ExampleObject(name = "이미 사용된 쿠폰", value = OrderSwaggerDocs.ALREADY_USED_COUPON)
                            })),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @PostMapping
    public ResponseEntity<Map<String, Object>> mockCreateOrder(@RequestBody @Valid OrderCreateRequest request) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("code", 201);
        response.put("status", "Created");
        response.put("message", "요청이 정상적으로 처리되었습니다.");
        response.put("data", new OrderCreateResponse(1L));

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}