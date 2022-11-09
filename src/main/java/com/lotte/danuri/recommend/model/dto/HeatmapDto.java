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
public class HeatmapDto {

    @Field("_id")
    @Id
    private ObjectId id;

    @Field("x")
    private Long x;

    @Field("y")
    private Long y;

    @Field("product_code")
    private String productCode;

    @Field("value")
    private Long value;


    @Field("created_at")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime createDate;
}