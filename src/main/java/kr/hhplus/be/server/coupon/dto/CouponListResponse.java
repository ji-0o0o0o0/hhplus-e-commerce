package kr.hhplus.be.server.coupon.dto;


import java.util.List;

public record CouponListResponse(
        Long userId,
        List<CouponResponse> coupons
) { }
