package com.lotte.danuri.recommend.controller;

import com.lotte.danuri.recommend.model.dto.HeatmapDto;
import com.lotte.danuri.recommend.service.HeatmapService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/heatmaps")
@RequiredArgsConstructor
@CrossOrigin("*")
public class HeatmapController {
    private final HeatmapService heatmapService;

    @PostMapping(value = "/click", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "상품 상세 화면 클릭된 좌표값 저장", notes = "사용자가 상품 상세 화면 내 클릭한 좌표를 저장한다.")
    public ResponseEntity<?> upsertClickXY(@RequestBody HeatmapDto heatmapDto){
        heatmapService.upsertClickXY(heatmapDto);
        return ResponseEntity.ok().build();
    }
}
