package kr.hhplus.be.server.domain.coupon;

import kr.hhplus.be.server.common.exception.ApiException;
import kr.hhplus.be.server.common.exception.ErrorCode;
import kr.hhplus.be.server.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CouponService {

    private final CouponRepository couponRepository;
    private final UserCouponRepository userCouponRepository;
    //쿠폰발급
    public  UserCoupon issueCouponTo(User user,Long couponId){
        //쿠폰 조회
        Coupon coupon = couponRepository.findByCouponId(couponId)
                .orElseThrow(()->new ApiException(ErrorCode.COUPON_NOT_FOUND));
        if(coupon.isExpired() || coupon.getStock()<=0) throw new ApiException(ErrorCode.COUPON_OUT_OF_STOCK);
        //발급 받았는지 조회
        Boolean alreadyIssued = userCouponRepository.existsByUserIdAndCouponId(user.getId(), couponId);
        if(alreadyIssued) throw new ApiException(ErrorCode.COUPON_ALREADY_USED);

        //쿠폰 발급, 유저 쿠폰 생성
        coupon.issuance();
        UserCoupon userCoupon = UserCoupon.create(user,coupon);
        //저장
        saveUserCoupon(userCoupon);
        return userCoupon;
    }
    //본인 소유 쿠폰 조회
    public List<UserCoupon> getUserCoupons(User user) {
        return userCouponRepository.findAllByUserId(user.getId());
    }
    //쿠폰 id로 본인 쿠폰 정보 조회
    public UserCoupon getUserCoupon(User user, Long couponId) {
        //쿠폰 조회
        Coupon coupon = couponRepository.findByCouponId(couponId)
                .orElseThrow(()->new ApiException(ErrorCode.COUPON_NOT_FOUND));
        if (coupon.isExpired()) {
            throw new ApiException(ErrorCode.COUPON_EXPIRED);
        }

        return  userCouponRepository.findCouponByUserIdAndCouponId(user.getId(), couponId)
                .orElseThrow(()->new ApiException(ErrorCode.INVALID_USER_COUPON));
    }
    //쿠폰 복원
    public void rollbackCoupon(UserCoupon userCoupon){
        userCoupon.rollback();
        saveUserCoupon(userCoupon);
    }
    public void saveUserCoupon(UserCoupon userCoupon) {
        userCouponRepository.save(userCoupon);
    }
}
