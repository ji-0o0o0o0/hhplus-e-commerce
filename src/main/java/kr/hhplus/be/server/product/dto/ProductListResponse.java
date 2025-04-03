package kr.hhplus.be.server.product.dto;


import java.util.List;

public record ProductListResponse(
        List<ProductResponse> products
) { }