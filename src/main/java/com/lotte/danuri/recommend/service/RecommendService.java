package com.lotte.danuri.recommend.service;

import org.apache.mahout.cf.taste.common.TasteException;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;

public interface RecommendService {
    List<Long> getRecommends(Long memberId) throws IOException, TasteException;

    void upsertClickLog(Long memberId, Long productId);
}
