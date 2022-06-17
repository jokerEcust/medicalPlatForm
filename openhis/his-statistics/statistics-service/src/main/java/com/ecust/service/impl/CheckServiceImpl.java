package com.ecust.service.impl;

import com.ecust.domain.Check;
import com.ecust.domain.CheckStat;
import com.ecust.dto.CheckQueryDto;
import com.ecust.mapper.CheckMapper;
import com.ecust.service.CheckService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class CheckServiceImpl implements CheckService {
    @Autowired
    CheckMapper checkMapper;
    @Override
    public List<Check> queryAllCheck(CheckQueryDto checkQueryDto) {
        return checkMapper.selectAllCheck(checkQueryDto);
    }

    @Override
    public List<CheckStat> queryAllCheckStat(CheckQueryDto checkQueryDto) {
        return checkMapper.selectAllCheckStat(checkQueryDto);

    }
}
