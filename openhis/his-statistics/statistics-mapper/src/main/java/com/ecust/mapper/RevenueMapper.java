package com.ecust.mapper;

import com.ecust.domain.Income;
import com.ecust.domain.Refund;
import com.ecust.dto.RevenueQueryDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RevenueMapper {
    List<Income> queryIncome(@Param("revenue") RevenueQueryDto revenueQueryDto);

    List<Refund> queryRefund(@Param("revenue") RevenueQueryDto revenueQueryDto);
}
