package kr.hhplus.be.server.application.point;

public record PointCommand(Long userId, Integer chargeAmount) {}