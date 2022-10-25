package com.lotte.danuri.recommend.controller;

import com.lotte.danuri.recommend.service.RecommendService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.apache.mahout.cf.taste.common.TasteException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.UnknownHostException;
import java.util.List;

@RestController
@RequestMapping(value = "/recommend")
@RequiredArgsConstructor
@CrossOrigin("*")
public class RecommendController {
    private final RecommendService recommendService;

    /*
    @PostMapping(value = "", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "상품 클릭", notes = "상품을 클릭한다.")
    public ResponseEntity<?> upsertClickLog(){

    }
    */

    @GetMapping(value = "", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "추천 상품 조회", notes = "추천 상품을 조회한다.")
    public ResponseEntity<?> getRecommends() throws UnknownHostException, TasteException {

        List<Long> recommendList = recommendService.getRecommends();
        return ResponseEntity.ok(recommendList);
    }
}
