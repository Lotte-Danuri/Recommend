package com.lotte.danuri.recommend.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class ProductDetailResponseDto {
    private Long id;

    private String productName;
    private String thumbnailUrl;
    private Double price;
    private Long stock;
    private Long storeId;
    private String storeName;
    private Long likeCount;
    private String productCode;
    private Long warranty;
    private String brandName;
    private String categoryFirstName;
    private String categorySecondName;
    private String categoryThirdName;

    private List<String> imageList;
}
