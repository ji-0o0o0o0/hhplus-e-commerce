package kr.hhplus.be.server.lagacy.product;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import kr.hhplus.be.server.lagacy.product.docs.ProductSwaggerDocs;
import kr.hhplus.be.server.lagacy.product.dto.BestProductResponse;
import kr.hhplus.be.server.lagacy.product.dto.ProductListResponse;
import kr.hhplus.be.server.lagacy.product.dto.ProductResponse;
import kr.hhplus.be.server.lagacy.product.dto.BestProductListResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    //상품 목록 조회
    @Operation(summary = "전체 상품 목록 조회", description = "상품 이름, 가격, 재고 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(name = "상품 목록", value = ProductSwaggerDocs.PRODUCT_LIST))),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(name = "서버 오류", value = ProductSwaggerDocs.INTERNAL_ERROR)))
    })
    @GetMapping
    public ResponseEntity<Map<String, Object>> mockGetProducts() {
        List<ProductResponse> productList = List.of(
                new ProductResponse(1L, "Macbook Pro", 2000000L, 10),
                new ProductResponse(2L, "iPhone 12", 1200000L, 20)
        );

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("code", 200);
        response.put("message", "요청이 정상적으로 처리되었습니다.");
        response.put("data", new ProductListResponse(productList));

        return ResponseEntity.ok(response);
    }

    //인기 상품 조회
    @Operation(summary = "인기 상품 조회", description = "최근 3일간 가장 많이 팔린 상위 5개 상품을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(name = "인기 상품", value = ProductSwaggerDocs.BEST_PRODUCTS))),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(name = "서버 오류", value = ProductSwaggerDocs.INTERNAL_ERROR)))
    })
    @GetMapping("/best")
    public ResponseEntity<Map<String, Object>> mockGetBestProducts() {
        List<BestProductResponse> bestList = List.of(
                new BestProductResponse(1L, "Ice Americano", 1000L, 100, 50),
                new BestProductResponse(2L, "iPhone 12", 1200000L, 90, 100),
                new BestProductResponse(3L, "Galaxy S24", 1100000L, 80, 70)
        );

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("code", 200);
        response.put("message", "요청이 정상적으로 처리되었습니다.");
        response.put("data", new BestProductListResponse(bestList));

        return ResponseEntity.ok(response);
    }
}