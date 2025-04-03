package kr.hhplus.be.server.product.dto;


import java.util.List;

public record BestProductListResponse(
        List<BestProductResponse> products
) { }
