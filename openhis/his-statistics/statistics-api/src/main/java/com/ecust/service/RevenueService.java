package com.ecust.service;

import com.ecust.dto.RevenueQueryDto;

import java.util.Map;

public interface RevenueService {
    Map<String, Object> queryAllRevenueData(RevenueQueryDto revenueQueryDto);
}
