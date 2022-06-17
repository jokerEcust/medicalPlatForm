package com.ecust.mapper;

import com.ecust.domain.Drug;
import com.ecust.domain.DrugStat;
import com.ecust.domain.Income;
import com.ecust.domain.Refund;
import com.ecust.dto.DrugQueryDto;
import com.ecust.dto.RevenueQueryDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DrugMapper {

    List<Drug> selectDrug(@Param("drug") DrugQueryDto drugQueryDto);

    List<DrugStat> selectDrugStat(@Param("drug") DrugQueryDto drugQueryDto);
}
