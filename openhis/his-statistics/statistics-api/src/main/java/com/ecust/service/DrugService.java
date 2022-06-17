package com.ecust.service;

import com.ecust.domain.Drug;
import com.ecust.domain.DrugStat;
import com.ecust.dto.DrugQueryDto;

import java.util.List;

public interface DrugService {

    List<Drug> queryDrug(DrugQueryDto drugQueryDto);

    List<DrugStat> queryDrugStat(DrugQueryDto drugQueryDto);
}
