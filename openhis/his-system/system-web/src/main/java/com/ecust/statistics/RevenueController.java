package com.ecust.statistics;

import cn.hutool.core.date.DateUtil;
import com.ecust.controller.BaseController;

import com.ecust.dto.RevenueQueryDto;
import com.ecust.service.RevenueService;
import com.ecust.vo.AjaxResult;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/statistics/revenue")
public class RevenueController extends BaseController {
    @Reference
    private RevenueService revenueService;
    @GetMapping("/queryAllRevenueData")
    public AjaxResult queryAllRevenueData(RevenueQueryDto revenueQueryDto) {
//如果没有开始或者结束日期就查询当天数据
        if (revenueQueryDto.getBeginTime() == null) {
            revenueQueryDto.setQueryDate(DateUtil.format(DateUtil.date(), "yyyy-MM-dd"));
        }
        Map<String,Object> map=revenueService.queryAllRevenueData(revenueQueryDto);
        return AjaxResult.success(map);
    }
}
