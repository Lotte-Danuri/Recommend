package com.lotte.danuri.recommend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class RecommendDto {

    @Field("_id")
    @Id
    private Long id;

    @Field("user_id")
    private Long memberId;

    @Field("item_id")
    private Long productId;

    @Field("preference")
    private Long clickCount;
}