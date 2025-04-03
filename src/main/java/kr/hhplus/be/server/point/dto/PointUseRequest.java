package kr.hhplus.be.server.point.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record PointUseRequest(
        @NotNull @Min(1) Long orderId
) { }