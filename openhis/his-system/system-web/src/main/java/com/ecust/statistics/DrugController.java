package com.ecust.statistics;

import cn.hutool.core.date.DateUtil;
import com.ecust.domain.Drug;
import com.ecust.dto.DrugQueryDto;
import com.ecust.service.DrugService;
import com.ecust.vo.AjaxResult;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/statistics/drug")
public class DrugController {
    @Reference
    private DrugService drugService;

    @GetMapping("/queryDrug")
    public AjaxResult queryDrug(DrugQueryDto drugQueryDto){
        if(drugQueryDto.getQueryDate()==null){
            drugQueryDto.setQueryDate(DateUtil.format(DateUtil.date(), "yyyy-MM-dd"));
        }
        List<Drug> drugs = drugService.queryDrug(drugQueryDto);

        return AjaxResult.success(drugs);
    }
    @GetMapping("/queryDrugStat")
    public AjaxResult queryDrugStat(DrugQueryDto drugQueryDto){
        if(drugQueryDto.getQueryDate()==null){
            drugQueryDto.setQueryDate(DateUtil.format(DateUtil.date(), "yyyy-MM-dd"));
        }
        return AjaxResult.success(drugService.queryDrugStat(drugQueryDto));
    }
}
