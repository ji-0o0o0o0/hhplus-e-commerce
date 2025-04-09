package kr.hhplus.be.server.lagacy.point.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record PointChargeRequest(
        @NotNull @Min(1) Long userId,
        @NotNull @Min(1) @Max(1_000_000) Long chargeAmount
) { }