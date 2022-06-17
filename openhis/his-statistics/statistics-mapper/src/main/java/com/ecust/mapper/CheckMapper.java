package com.ecust.mapper;

import com.ecust.domain.Check;
import com.ecust.domain.CheckStat;
import com.ecust.dto.CheckQueryDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CheckMapper {
    List<Check> selectAllCheck(@Param("check") CheckQueryDto checkQueryDto);

    List<CheckStat> selectAllCheckStat(@Param("check") CheckQueryDto checkQueryDto);
}
