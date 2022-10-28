package com.lotte.danuri.recommend.repository;

import com.lotte.danuri.recommend.model.dto.RecommendDto;
import com.lotte.danuri.recommend.model.dto.RecommendSelectDto;
import com.lotte.danuri.recommend.model.dto.request.ProductListDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class RecommendDao {
    @Autowired
    private MongoTemplate mongoTemplate;

    public void upsertClickLog(Long memberId, Long productId) {
        Query query = new Query();

        query.addCriteria(Criteria.where("user_id").is(memberId));
        query.addCriteria(Criteria.where("item_id").is(productId));

        RecommendDto dto = mongoTemplate.findOne(query, RecommendDto.class, "productClick");

        if(dto==null){

            mongoTemplate.insert(new RecommendDto(null, memberId, productId, 1D, LocalDateTime.now()), "productClick");
        }
        else{
            Update update = new Update();
            update.set("preference",dto.getClickCount()+1);

            mongoTemplate.updateFirst(query, update, RecommendDto.class, "productClick");
        }
    }

    public List<RecommendSelectDto> selectAllClickLog() {
        Query query = new Query();
        query.fields().exclude("_id");
        query.fields().exclude("created_at");

        query.addCriteria(Criteria.where("user_id").ne(0L));

        return mongoTemplate.find(query, RecommendSelectDto.class, "productClick");
    }

    public Long selectClickCount(Long productId) {
        Query query = new Query();
        query.fields().include("preference");
        query.addCriteria(Criteria.where("item_id").is(productId));

        List<RecommendSelectDto> recommendSelectDtoList = mongoTemplate.find(query, RecommendSelectDto.class, "productClick");

        Double result = recommendSelectDtoList.stream()
                              .map(recommendSelectDto -> recommendSelectDto.getC_clickCount())
                              .reduce(0D, Double::sum);

        return result.longValue();
    }
}
