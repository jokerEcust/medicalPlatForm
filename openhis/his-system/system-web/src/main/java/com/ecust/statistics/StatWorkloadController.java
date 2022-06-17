package com.ecust.statistics;

import cn.hutool.core.date.DateUtil;
import com.ecust.domain.Workload;
import com.ecust.domain.WorkloadStat;
import com.ecust.dto.WorkloadQueryDto;
import com.ecust.service.WorkloadService;
import com.ecust.vo.AjaxResult;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
@RestController
@RequestMapping("statistics/workload")
public class StatWorkloadController {

    @Reference
    private WorkloadService workloadService;

    /**
     * 医生工作量统计列表
     */
    @GetMapping("queryWorkload")
    public AjaxResult queryWorkload(WorkloadQueryDto workloadQueryDto){
        if(workloadQueryDto.getBeginTime()==null){
            workloadQueryDto.setQueryDate(DateUtil.format(DateUtil.date(),"yyyy-MM-dd"));
        }
        List<Workload> workloadList=this.workloadService.queryWorkload(workloadQueryDto);
        return AjaxResult.success(workloadList);
    }

    /**
     * 医生工作量统计列表
     */
    @GetMapping("queryWorkloadStat")
    public AjaxResult queryWorkloadStat(WorkloadQueryDto workloadQueryDto){
        if(workloadQueryDto.getBeginTime()==null){
            workloadQueryDto.setQueryDate(DateUtil.format(DateUtil.date(),"yyyy-MM-dd"));
        }
        List<WorkloadStat> workloadList=this.workloadService.queryWorkloadStat(workloadQueryDto);
        return AjaxResult.success(workloadList);
    }
}
