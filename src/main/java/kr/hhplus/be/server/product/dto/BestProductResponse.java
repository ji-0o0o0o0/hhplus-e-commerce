package kr.hhplus.be.server.product.dto;

public record BestProductResponse(
        Long id,
        String name,
        Long price,
        Integer sales,
        Integer stock
) { }