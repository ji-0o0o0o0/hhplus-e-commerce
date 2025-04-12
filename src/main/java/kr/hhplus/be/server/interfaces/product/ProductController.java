package kr.hhplus.be.server.interfaces.product;

import kr.hhplus.be.server.application.product.ProductFacade;
import kr.hhplus.be.server.application.product.ProductResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import kr.hhplus.be.server.domain.product.Product;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private ProductFacade productFacade;

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        return ResponseEntity.ok(
                productFacade.getAllProducts().stream()
                        .map(ProductResponse::fromResult)
                        .toList()
        );
    }

}
