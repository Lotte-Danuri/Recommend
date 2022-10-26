package com.lotte.danuri.recommend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Document
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class RecommendSelectDto {

    @Field("user_id")
    private Long a_memberId;

    @Field("item_id")
    private Long b_productId;

    @Field("preference")
    private Double c_clickCount;
}