package com.lotte.danuri.recommend.service;

import com.lotte.danuri.recommend.model.dto.HeatmapDto;

public interface HeatmapService {
    void upsertClickXY(HeatmapDto heatmapDto);
}
