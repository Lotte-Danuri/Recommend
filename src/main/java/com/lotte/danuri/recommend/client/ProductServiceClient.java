package com.lotte.danuri.recommend.client;

import com.lotte.danuri.recommend.model.dto.ProductDto;
import com.lotte.danuri.recommend.model.dto.request.ProductListDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "product")
public interface ProductServiceClient {

    @PostMapping("/products/list")
    List<ProductDto> getProductList(@RequestBody ProductListDto productListDto);
}