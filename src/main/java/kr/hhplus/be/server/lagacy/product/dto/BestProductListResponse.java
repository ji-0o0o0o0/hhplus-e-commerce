package kr.hhplus.be.server.lagacy.product.dto;


import java.util.List;

public record BestProductListResponse(
        List<BestProductResponse> products
) { }
