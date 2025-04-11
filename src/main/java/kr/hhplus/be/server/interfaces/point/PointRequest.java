package kr.hhplus.be.server.interfaces.point;

import kr.hhplus.be.server.application.point.PointCommand;

public record PointRequest(Long userId, Integer chargeAmount) {
    public PointCommand toCommand() {
        return new PointCommand(userId, chargeAmount);
    }
}