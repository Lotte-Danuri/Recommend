package com.lotte.danuri.recommend.service;

import com.lotte.danuri.recommend.repository.RecommendDao;
import lombok.RequiredArgsConstructor;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.mongodb.MongoDBDataModel;
import org.apache.mahout.cf.taste.impl.recommender.GenericBooleanPrefItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.LogLikelihoodSimilarity;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecommendServiceImpl implements RecommendService{
    private final Environment env;
    private final RecommendDao recommendDao;
    @Override
    public List<Long> getRecommends() throws UnknownHostException, TasteException {
        /*
        List<RecommendDto> recommendDtoList = new ArrayList<>();
        recommendDtoList.add(new RecommendDto(1L,2L,3L));
        recommendDtoList.add(new RecommendDto(1L,3L,5L));
        recommendDtoList.add(new RecommendDto(2L,1L,6L));
        recommendDtoList.add(new RecommendDto(2L,3L,8L));
        recommendDtoList.add(new RecommendDto(2L,7L,100L));
        recommendDtoList.add(new RecommendDto(3L,2L,55L));
        recommendDtoList.add(new RecommendDto(3L,10L,99L));
        */

        //MongoDBDataModel을 이용해 MongoDB로부터 추천 데이터를 Load
        MongoDBDataModel dataModel = new MongoDBDataModel(
                env.getProperty("danurimongo.host"),
                27017,
                "product_order",
                "productClick",
                false,
                false,
                null
        );

        //DataModel을 바탕으로 LogLikelihoodSimilarity를 초기화
        ItemSimilarity similarity = new LogLikelihoodSimilarity(dataModel);

        //생성된 DataModel과 LogLikelihoodSimilarity를 이용해 Recommender를 생성
        Recommender recommender = new GenericBooleanPrefItemBasedRecommender(dataModel, similarity);

        //Recommender의 recommend() method를 이용해 아이템을 추천 받음
        List<RecommendedItem> recommended = recommender.recommend(1,3);
        System.out.println(recommended);
        return new ArrayList<>();
    }
}
