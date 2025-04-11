package kr.hhplus.be.server.lagacy.product.dto;

public record ProductResponse(
        Long id,
        String name,
        Long price,
        Integer stock
) { }