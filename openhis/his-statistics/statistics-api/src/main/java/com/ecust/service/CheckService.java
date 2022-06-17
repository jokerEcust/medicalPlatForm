package com.ecust.service;

import com.ecust.domain.Check;
import com.ecust.domain.CheckStat;
import com.ecust.dto.CheckQueryDto;

import java.util.List;

public interface CheckService {
    List<Check> queryAllCheck(CheckQueryDto checkQueryDto);

    List<CheckStat> queryAllCheckStat(CheckQueryDto checkQueryDto);
}
