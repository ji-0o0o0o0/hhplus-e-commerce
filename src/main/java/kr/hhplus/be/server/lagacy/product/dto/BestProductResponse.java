package kr.hhplus.be.server.lagacy.product.dto;

public record BestProductResponse(
        Long id,
        String name,
        Long price,
        Integer sales,
        Integer stock
) { }