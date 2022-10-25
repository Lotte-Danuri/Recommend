package com.lotte.danuri.recommend.service;

import org.apache.mahout.cf.taste.common.TasteException;

import java.net.UnknownHostException;
import java.util.List;

public interface RecommendService {
    List<Long> getRecommends() throws UnknownHostException, TasteException;
}
