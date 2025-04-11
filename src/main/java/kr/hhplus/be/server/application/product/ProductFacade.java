package kr.hhplus.be.server.application.product;

import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductService;
import kr.hhplus.be.server.interfaces.product.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductFacade implements ProductUseCase{

    private final ProductService productService;

    //전체 상품 조회
    @Override
    public List<ProductResult> getAllProducts() {
        return productService.findAll().stream()
                .map(ProductResult::from)
                .toList();
    }
}
