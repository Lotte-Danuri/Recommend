package com.lotte.danuri.recommend.service;

import com.lotte.danuri.recommend.model.dto.ProductDto;
import com.lotte.danuri.recommend.model.dto.request.MemberListDto;
import com.lotte.danuri.recommend.model.dto.request.ProductListDto;
import com.lotte.danuri.recommend.model.dto.response.ProductCodeList;
import org.apache.mahout.cf.taste.common.TasteException;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;

public interface RecommendService {
    List<ProductDto> getRecommends(Long memberId) throws IOException, TasteException;

    void upsertClickLog(Long memberId, Long productId);

    List<Long> getClickCount(ProductListDto productListDto);

    List<Long> getClickCountByDate(ProductListDto productListDto);

    List<ProductCodeList> getRecommendsByMemberId(List<MemberListDto> memberListDtos) throws IOException, TasteException;
}
