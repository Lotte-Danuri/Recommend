package com.lotte.danuri.recommend.controller;

import com.lotte.danuri.recommend.model.dto.ProductDto;
import com.lotte.danuri.recommend.model.dto.request.ProductListDto;
import com.lotte.danuri.recommend.service.RecommendService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.apache.mahout.cf.taste.common.TasteException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;

@RestController
@RequestMapping(value = "/recommends")
@RequiredArgsConstructor
@CrossOrigin("*")
public class RecommendController {
    private final RecommendService recommendService;

    @GetMapping(value = "/click/{memberId}/{productId}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "상품 클릭", notes = "상품을 클릭한다.")
    public ResponseEntity<?> upsertClickLog(@PathVariable("memberId") Long memberId, @PathVariable("productId") Long productId){
        recommendService.upsertClickLog(memberId, productId);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/list/{memberId}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "추천 상품 조회", notes = "추천 상품을 조회한다.")
    public ResponseEntity<?> getRecommends(@PathVariable("memberId") Long memberId) throws IOException, TasteException {
        List<ProductDto> recommendList = recommendService.getRecommends(memberId);
        return ResponseEntity.ok(recommendList);
    }

    @PostMapping(value = "/click/count", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "클릭 총 횟수 조회 ", notes = "상품 ID 리스트에 의해 클릭 횟수 리스트를 조회한다.")
    public ResponseEntity<?> getClickCount(@RequestBody ProductListDto productListDto){
        List<Long> clickCount = recommendService.getClickCount(productListDto);
        return ResponseEntity.ok(clickCount);
    }

}