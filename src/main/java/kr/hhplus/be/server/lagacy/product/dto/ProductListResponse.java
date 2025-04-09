package kr.hhplus.be.server.lagacy.product.dto;


import java.util.List;

public record ProductListResponse(
        List<ProductResponse> products
) { }