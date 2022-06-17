package com.ecust.service;

import com.ecust.domain.OrderCharge;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ecust.domain.OrderChargeItem;
import com.ecust.dto.OrderChargeDto;
import com.ecust.dto.OrderChargeFormDto;
import com.ecust.vo.DataGridView;

import java.util.List;

public interface OrderChargeService{


    void saveOrderAndOrderItems(OrderChargeFormDto orderChargeFormDto);

    /**
     * 支付成功更新支付状态
     * @param orderId 订单id
     * @param payPlatformId 平台交易id。现金：null
     */
    void paySuccess(String orderId, String payPlatformId);

    OrderCharge queryOrderChargeByOrderId(String orderId);

    DataGridView queryAllOrderChargeForPage(OrderChargeDto orderChargeDto);

    List<OrderChargeItem> queryOrderChargeItemByOrderId(String orderId);

    OrderChargeItem queryOrderChargeItemByItemId(String itemId);
}
