package com.ecust.service.impl;

import com.ecust.domain.Drug;
import com.ecust.domain.DrugStat;
import com.ecust.dto.DrugQueryDto;
import com.ecust.mapper.DrugMapper;
import com.ecust.service.DrugService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


@Service
public class DrugServiceImpl implements DrugService {
    @Autowired
    DrugMapper drugMapper;
    @Override
    public List<Drug> queryDrug(DrugQueryDto drugQueryDto) {
        return drugMapper.selectDrug(drugQueryDto);
    }

    @Override
    public List<DrugStat> queryDrugStat(DrugQueryDto drugQueryDto) {
        return drugMapper.selectDrugStat(drugQueryDto);
    }
}
