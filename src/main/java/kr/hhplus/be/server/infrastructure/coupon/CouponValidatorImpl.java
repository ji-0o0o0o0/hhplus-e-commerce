package kr.hhplus.be.server.infrastructure.coupon;

import kr.hhplus.be.server.domain.order.CouponValidator;
import org.springframework.stereotype.Component;

@Component
public class CouponValidatorImpl implements CouponValidator {

    @Override
    public void validateCouponOwnership(Long userId, Long couponId) {
        // TODO: 실제 검증 로직 or mock
        System.out.println("쿠폰 소유 검증 로직 실행");
    }
}