package kr.hhplus.be.server.lagacy.coupon.dto;


import java.util.List;

public record CouponListResponse(
        Long userId,
        List<CouponResponse> coupons
) { }
