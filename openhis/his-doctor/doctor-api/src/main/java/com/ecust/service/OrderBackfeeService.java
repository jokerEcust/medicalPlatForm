package com.ecust.service;

import com.ecust.domain.OrderBackfee;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ecust.domain.OrderBackfeeItem;
import com.ecust.dto.OrderBackfeeDto;
import com.ecust.dto.OrderBackfeeFormDto;
import com.ecust.vo.DataGridView;

public interface OrderBackfeeService{

    void saveOrderAndItems(OrderBackfeeFormDto orderBackfeeFormDto);

    void backSuccess(String backId, String backPlatformId, String payType0);

    DataGridView queryOrderBackfeeForPage(OrderBackfeeDto orderBackfeeDto);

    OrderBackfeeItem queryOrderBackfeeItemByBackId(String backId);
}
