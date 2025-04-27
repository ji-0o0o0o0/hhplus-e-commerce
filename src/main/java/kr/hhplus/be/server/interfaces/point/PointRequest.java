package kr.hhplus.be.server.interfaces.point;

import kr.hhplus.be.server.application.point.PointCommand;

public record PointRequest() {

    public record Charge(
            long userId,
            long amount
    ) {
    }


}