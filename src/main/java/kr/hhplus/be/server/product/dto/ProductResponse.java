package kr.hhplus.be.server.product.dto;

public record ProductResponse(
        Long id,
        String name,
        Long price,
        Integer stock
) { }