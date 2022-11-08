package com.lotte.danuri.recommend.service;

import com.lotte.danuri.recommend.client.ProductServiceClient;
import com.lotte.danuri.recommend.model.dto.ProductDto;
import com.lotte.danuri.recommend.model.dto.RecommendSelectDto;
import com.lotte.danuri.recommend.model.dto.request.MemberListDto;
import com.lotte.danuri.recommend.model.dto.request.ProductListDto;
import com.lotte.danuri.recommend.model.dto.response.ProductCodeList;
import com.lotte.danuri.recommend.model.dto.response.ProductDetailResponseDto;
import com.lotte.danuri.recommend.repository.RecommendDao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.ThresholdUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.UncenteredCosineSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.UserBasedRecommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static com.lotte.danuri.recommend.util.FileOut.csvFileOut;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecommendServiceImpl implements RecommendService{
    private final Environment env;
    private final RecommendDao recommendDao;
    private final ProductServiceClient productServiceClient;
    private final CircuitBreakerFactory circuitBreakerFactory;
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
        List<RecommendedItem> recommended = new ArrayList<>();
        if (recommendDao.existMember(memberId)) {
            recommended = recommender.recommend(memberId, 5);
        }

        recommended.forEach(v -> {
            productRecommendedIds.add(v.getItemID());
        });

        log.info("Before Call [getProductListById] Method IN [Recommend-Service]");
        List<ProductDto> productDtoList = productServiceClient.getProductListById(ProductListDto.builder()
                        .productId(productRecommendedIds)
                        .build());
        log.info("After Call [getProductListById] Method IN [Recommend-Service]");
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

    @Override
    public List<ProductCodeList> getRecommendsByMemberId(List<MemberListDto> memberListDtos) throws IOException, TasteException {
        List<ProductCodeList> productCodeLists = new ArrayList<>();

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

        memberListDtos.forEach(v -> {
            /* memrberId를 통해 4개의 아이템을 추천 */
            try {
                if (recommendDao.existMember(v.getMemberId())) {
                    List<RecommendedItem> recommended = recommender.recommend(v.getMemberId(), 4);
                    StringBuilder sb = new StringBuilder();
                    recommended.forEach(w -> {
                        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitBreaker");
                        ProductDetailResponseDto productDetailResponseDto = circuitBreaker.run(() -> productServiceClient.getProduct(w.getItemID()),
                                throwable -> new ProductDetailResponseDto());
                        sb.append(productDetailResponseDto.getProductCode());
                        sb.append("/");
                    });
                    String productCode = sb.toString();
                    productCodeLists.add(ProductCodeList.builder().productCode(productCode.substring(0, productCode.length() - 1)).loginId(v.getLoginId()).build());
                }
            } catch (TasteException e) {
                throw new RuntimeException(e);
            }
        });
        return productCodeLists;
    }
}