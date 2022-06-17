package com.ecust.statistics;

import cn.hutool.core.date.DateUtil;
import com.ecust.controller.BaseController;
import com.ecust.dto.CheckQueryDto;
import com.ecust.service.CheckService;
import com.ecust.vo.AjaxResult;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/statistics/check")
public class StatCheckController extends BaseController {
    @Reference
    CheckService checkService;
    @GetMapping("/queryCheck")
    public AjaxResult queryCheck(CheckQueryDto checkQueryDto){
        if(checkQueryDto.getBeginTime()==null){
            checkQueryDto.setQueryDate(DateUtil.format(DateUtil.date(),"yyyy-MM-dd"));
        }
        return AjaxResult.success(checkService.queryAllCheck(checkQueryDto));
    }

    @GetMapping("/queryCheckStat")
    public AjaxResult queryCheckStat(CheckQueryDto checkQueryDto){
        if(checkQueryDto.getBeginTime()==null){
            checkQueryDto.setQueryDate(DateUtil.format(DateUtil.date(),"yyyy-MM-dd"));
        }
        return AjaxResult.success(checkService.queryAllCheckStat(checkQueryDto));
    }

}
