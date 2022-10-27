package com.lotte.danuri.recommend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class ProductDto {

    private Long id;

    private String productName;
    private String thumbnailUrl;
    private Double price;
    private Long stock;
    private Long storeId;
    private Long likeCount;
    private String productCode;
    private Long warranty;

    private Long categoryFirstId;
    private Long categorySecondId;
    private Long categoryThirdId;

    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    private String createdBy;
    private String updatedBy;
}