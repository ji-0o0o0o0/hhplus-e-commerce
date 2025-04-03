package kr.hhplus.be.server.coupon.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CouponIssueRequest(
        @NotNull @Min(1) Long userId
) { }