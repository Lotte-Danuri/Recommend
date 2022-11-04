package com.lotte.danuri.recommend.service;

import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.csv.CsvGenerator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.lotte.danuri.recommend.client.ProductServiceClient;
import com.lotte.danuri.recommend.model.dto.ProductDto;
import com.lotte.danuri.recommend.model.dto.RecommendSelectDto;
import com.lotte.danuri.recommend.model.dto.request.ProductListDto;
import com.lotte.danuri.recommend.repository.RecommendDao;
import com.lotte.danuri.recommend.util.FileOut;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.ThresholdUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericBooleanPrefItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.EuclideanDistanceSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.LogLikelihoodSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.UncenteredCosineSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.recommender.UserBasedRecommender;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.apache.mahout.math.hadoop.similarity.cooccurrence.measures.CosineSimilarity;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import static com.lotte.danuri.recommend.util.FileOut.csvFileOut;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecommendServiceImpl implements RecommendService{
    private final Environment env;
    private final RecommendDao recommendDao;
    private final ProductServiceClient productServiceClient;
    @Override
    public List<ProductDto> getRecommends(Long memberId) throws IOException, TasteException {

        List<Long> productRecommendedIds = new ArrayList<>();
        List<RecommendSelectDto> recommendList = recommendDao.selectAllClickLog();
        File csvFile = new File("Clickdata.csv");
        csvFileOut(RecommendSelectDto.class, csvFile, recommendList);

        /* 데이터 모델 생성 */
        DataModel dataModel = new FileDataModel(csvFile);

        /* 유사도 모델 생성 (코사인유사도 사용)*/
        UserSimilarity similarity = new UncenteredCosineSimilarity(dataModel);

        /* 모든 유저들로부터 주어진 유저와 특정 임계값을 충족하거나 초과하는 neighborhood 기준 */
        UserNeighborhood neighborhood = new ThresholdUserNeighborhood(0.1,similarity, dataModel);

        /* 사용자 추천기 생성 */
        UserBasedRecommender recommender = new GenericUserBasedRecommender(dataModel, neighborhood, similarity);

        /* memrberId를 통해 5개의 아이템을 추천 */
        List<RecommendedItem> recommended = recommender.recommend(memberId,5);

        recommended.forEach(v -> {
            productRecommendedIds.add(v.getItemID());
        });

        List<ProductDto> productDtoList = productServiceClient.getProductList(ProductListDto.builder()
                        .productId(productRecommendedIds)
                        .build());
        return productDtoList;
    }

    @Override
    public void upsertClickLog(Long memberId, Long productId){
        recommendDao.upsertClickLog(memberId, productId);
    }

    @Override
    public List<Long> getClickCount(ProductListDto productListDto){
        log.info("Before Retrieve [getClickCount] Method IN [Recommend-Service]");
        List<Long> result = new ArrayList<>();
        productListDto.getProductId().forEach(v -> {
            result.add(recommendDao.selectClickCount(v));
        });
        log.info("After Retrieve [getClickCount] Method IN [Recommend-Service]");
        return result;
    }

    @Override
    public List<Long> getClickCountByDate(ProductListDto productListDto){
        log.info("Before Retrieve [getClickCount] Method IN [Recommend-Service]");
        List<Long> result = new ArrayList<>();
        productListDto.getProductId().forEach(v -> {
            result.add(recommendDao.selectClickCountByDate(v,productListDto.getStartDate(),productListDto.getEndDate()));
        });
        log.info("After Retrieve [getClickCount] Method IN [Recommend-Service]");
        return result;
    }
}