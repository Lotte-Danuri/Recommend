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

    @GetMapping(value = "/click/login/{productId}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "상품 회원 클릭", notes = "회원이 상품을 클릭한다.")
    public ResponseEntity<?> upsertClickLog(@RequestHeader(value = "memberId") Long memberId, @PathVariable("productId") Long productId){
        recommendService.upsertClickLog(memberId, productId);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/click/unlogin/{productId}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "상품 비회원 클릭", notes = "비회원이 상품을 클릭한다.")
    public ResponseEntity<?> upsertClickLog(@PathVariable("productId") Long productId){
        recommendService.upsertClickLog(0L, productId);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/list", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "추천 상품 조회", notes = "추천 상품을 조회한다.")
    public ResponseEntity<?> getRecommends(@RequestHeader(value = "memberId") Long memberId) throws IOException, TasteException {
        List<ProductDto> recommendList = recommendService.getRecommends(memberId);
        return ResponseEntity.ok(recommendList);
    }

    @PostMapping(value = "/click/count", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "클릭 총 횟수 조회 ", notes = "상품 ID 리스트에 의해 클릭 횟수 리스트를 조회한다.")
    public ResponseEntity<?> getClickCount(@RequestBody ProductListDto productListDto){
        List<Long> clickCount = recommendService.getClickCount(productListDto);
        return ResponseEntity.ok(clickCount);
    }

    @PostMapping(value = "/click/date/count", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "기간 별 클릭 총 횟수 조회 ", notes = "기간과 상품 ID 리스트에 의해 클릭 횟수 리스트를 조회한다.")
    public ResponseEntity<?> getClickCountByDate(@RequestBody ProductListDto productListDto){
        List<Long> clickCount = recommendService.getClickCountByDate(productListDto);
        return ResponseEntity.ok(clickCount);
    }
}