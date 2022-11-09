package com.lotte.danuri.recommend.service;

import com.lotte.danuri.recommend.model.dto.HeatmapDto;
import com.lotte.danuri.recommend.repository.HeatmapDao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class HeatmapServiceImpl implements  HeatmapService{
    private final HeatmapDao heatmapDao;
    @Override
    @Async
    public void upsertClickXY(HeatmapDto heatmapDto){
        heatmapDao.upsertClickXY(heatmapDto);
    }
}
