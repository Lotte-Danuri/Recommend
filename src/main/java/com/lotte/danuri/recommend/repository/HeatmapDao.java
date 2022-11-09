package com.lotte.danuri.recommend.repository;

import com.lotte.danuri.recommend.model.dto.HeatmapDto;
import com.lotte.danuri.recommend.model.dto.RecommendDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

import static org.springframework.data.mongodb.core.query.Criteria.where;

@Repository
public class HeatmapDao {

    @Autowired
    private MongoTemplate mongoTemplate;

    public void upsertClickXY(HeatmapDto heatmapDto) {
        Query query = new Query();

        query.addCriteria(where("x").is(heatmapDto.getX()));
        query.addCriteria(where("y").is(heatmapDto.getY()));
        query.addCriteria(where("product_code").is(heatmapDto.getProductCode()));

        HeatmapDto dto = mongoTemplate.findOne(query, HeatmapDto.class, "heatmapClick");

        if(dto==null){
            mongoTemplate.insert(new HeatmapDto(null, heatmapDto.getX(), heatmapDto.getY(), heatmapDto.getProductCode(), 1L, LocalDateTime.now()), "heatmapClick");
        }
        else{
            Update update = new Update();
            update.set("value",dto.getValue()+1);

            mongoTemplate.updateFirst(query, update, HeatmapDto.class, "heatmapClick");
        }
    }
}