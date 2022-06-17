package com.ecust.service;

import com.ecust.domain.Workload;
import com.ecust.domain.WorkloadStat;
import com.ecust.dto.WorkloadQueryDto;

import java.util.List;

public interface WorkloadService {
    /**
     * 医生工作量统计列表
     * @param workloadQueryDto
     * @return
     */
    List<Workload> queryWorkload(WorkloadQueryDto workloadQueryDto);
    /**
     * 总体工作量统计列表
     * @param workloadQueryDto
     * @return
     */
    List<WorkloadStat> queryWorkloadStat(WorkloadQueryDto workloadQueryDto);
}
