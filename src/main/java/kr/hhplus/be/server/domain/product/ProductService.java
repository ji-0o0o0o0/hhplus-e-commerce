package kr.hhplus.be.server.domain.product;


import kr.hhplus.be.server.common.exception.ApiException;
import kr.hhplus.be.server.common.exception.ErrorCode;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;

    //상품 조회
    public Product getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.PRODUCT_NOT_FOUND));
        /*// 지금 주문 가능한지 확인
        if (product.getStock() <= 0) {
            throw new ApiException(ErrorCode.PRODUCT_OUT_OF_STOCK);
        }*/
        return product;
    }
    //모든 상품 조회
    public List<Product> findAll() {
        return productRepository.findAll();
    }
}