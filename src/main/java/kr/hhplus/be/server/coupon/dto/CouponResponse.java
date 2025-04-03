package kr.hhplus.be.server.coupon.dto;

import java.time.LocalDate;

public record CouponResponse(
        Long id,
        String title,
        String discountType, // "RATE" or "AMOUNT"
        Integer discountValue,
        LocalDate startDate,
        LocalDate endDate
) { }