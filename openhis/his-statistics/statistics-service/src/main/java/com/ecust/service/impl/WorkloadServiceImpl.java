package com.ecust.service.impl;

import com.ecust.domain.Workload;
import com.ecust.domain.WorkloadStat;
import com.ecust.dto.WorkloadQueryDto;
import com.ecust.mapper.WorkloadMapper;
import com.ecust.service.WorkloadService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


@Service
public class WorkloadServiceImpl implements WorkloadService {
    @Autowired
    private WorkloadMapper workloadMapper;
    @Override
    public List<Workload> queryWorkload(WorkloadQueryDto workloadQueryDto) {
        return workloadMapper.queryWorkload(workloadQueryDto);
    }

    @Override
    public List<WorkloadStat> queryWorkloadStat(WorkloadQueryDto workloadQueryDto) {
        return workloadMapper.queryWorkloadStat(workloadQueryDto);

    }
}
