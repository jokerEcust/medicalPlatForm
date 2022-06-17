package com.ecust.mapper;

import com.ecust.domain.Workload;
import com.ecust.domain.WorkloadStat;
import com.ecust.dto.WorkloadQueryDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface WorkloadMapper {
    List<Workload> queryWorkload(@Param("workload") WorkloadQueryDto workloadQueryDto);

    List<WorkloadStat> queryWorkloadStat(@Param("workload") WorkloadQueryDto workloadQueryDto);

}
