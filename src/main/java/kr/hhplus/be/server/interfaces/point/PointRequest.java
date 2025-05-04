package kr.hhplus.be.server.interfaces.point;

import kr.hhplus.be.server.application.point.PointCommand;

import java.math.BigDecimal;

public record PointRequest() {

    public record Charge(
            long userId,
            BigDecimal amount
    ) {
    }


}