package com.lotte.danuri.recommend.client;

import com.lotte.danuri.recommend.model.dto.ProductDto;
import com.lotte.danuri.recommend.model.dto.request.ProductListDto;
import com.lotte.danuri.recommend.model.dto.response.ProductDetailResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "product")
public interface ProductServiceClient {

    @PostMapping("/products/productId")
    List<ProductDto> getProductListById(@RequestBody ProductListDto productListDto);

    @GetMapping("/products/{productId}")
    ProductDetailResponseDto getProduct(@PathVariable Long productId);
}