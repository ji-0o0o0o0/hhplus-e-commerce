package kr.hhplus.be.server.interfaces.point;

import kr.hhplus.be.server.application.point.PointResult;

public record PointResponse(Long userId, Integer balance) {
    public static PointResponse from(PointResult result) {
        return new PointResponse(result.userId(), result.balance());
    }
}
