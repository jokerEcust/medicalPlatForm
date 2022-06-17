package com.ecust.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ecust.constants.Constants;
import com.ecust.domain.CareOrderItem;
import com.ecust.domain.OrderCharge;
import com.ecust.domain.OrderChargeItem;
import com.ecust.dto.OrderChargeDto;
import com.ecust.dto.OrderChargeFormDto;
import com.ecust.dto.OrderChargeItemDto;
import com.ecust.mapper.CareOrderItemMapper;
import com.ecust.mapper.OrderChargeItemMapper;
import com.ecust.vo.DataGridView;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Method;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.ecust.mapper.OrderChargeMapper;
import com.ecust.service.OrderChargeService;

import java.util.ArrayList;
import java.util.List;

@Service(methods = {@Method(name = "saveOrderAndOrderItems", retries = 1), @Method(name = "paySuccess", retries = 1)})
public class OrderChargeServiceImpl implements OrderChargeService {

    @Autowired
    OrderChargeMapper orderChargeMapper;

    @Autowired
    OrderChargeItemMapper orderChargeItemMapper;

    @Autowired
    CareOrderItemMapper careOrderItemMapper;

    @Override
    public void saveOrderAndOrderItems(OrderChargeFormDto orderChargeFormDto) {
        OrderChargeDto orderChargeDto = orderChargeFormDto.getOrderChargeDto();
        List<OrderChargeItemDto> orderChargeItemDtoList = orderChargeFormDto.getOrderChargeItemDtoList();

        OrderCharge orderCharge = new OrderCharge();
        BeanUtil.copyProperties(orderChargeDto, orderCharge);
        orderCharge.setOrderStatus(Constants.ORDER_STATUS_0);
        orderCharge.setCreateBy(orderChargeFormDto.getSimpleUser().getUserName());
        orderCharge.setCreateTime(DateUtil.date());
        int i = orderChargeMapper.insert(orderCharge);
        for (OrderChargeItemDto orderChargeItemDto : orderChargeItemDtoList) {
            OrderChargeItem orderChargeItem = new OrderChargeItem();
            BeanUtil.copyProperties(orderChargeItemDto, orderChargeItem);
            orderChargeItem.setOrderId(orderCharge.getOrderId());
            orderChargeItem.setStatus(Constants.ORDER_DETAILS_STATUS_0);
            orderChargeItemMapper.insert(orderChargeItem);
        }
    }

    @Override
    public void paySuccess(String orderId, String payPlatformId) {
        OrderCharge orderCharge = orderChargeMapper.selectById(orderId);
        orderCharge.setPayPlatformId(payPlatformId);
        orderCharge.setOrderStatus(Constants.ORDER_STATUS_1);
        orderChargeMapper.updateById(orderCharge);

        QueryWrapper<OrderChargeItem> qw = new QueryWrapper<>();
        qw.eq(OrderChargeItem.COL_ORDER_ID, orderId);
        List<OrderChargeItem> orderChargeItems = orderChargeItemMapper.selectList(qw);
        List<String> allItemIds = new ArrayList<>();
        for (OrderChargeItem orderChargeItem : orderChargeItems) {
            allItemIds.add(orderChargeItem.getItemId());
        }
        OrderChargeItem orderItemObj = new OrderChargeItem();
        orderItemObj.setStatus(Constants.ORDER_DETAILS_STATUS_1);
        QueryWrapper<OrderChargeItem> orderItemQw = new QueryWrapper<>();
        orderItemQw.in(OrderChargeItem.COL_ITEM_ID, allItemIds);
        this.orderChargeItemMapper.update(orderItemObj, orderItemQw);
        //更新处方详情的状态
        CareOrderItem careItemObj = new CareOrderItem();
        careItemObj.setStatus(Constants.ORDER_DETAILS_STATUS_1);
        QueryWrapper<CareOrderItem> careItemQw = new QueryWrapper<>();
        careItemQw.in(CareOrderItem.COL_ITEM_ID, allItemIds);
        this.careOrderItemMapper.update(careItemObj, careItemQw);
    }

    @Override
    public OrderCharge queryOrderChargeByOrderId(String orderId) {
        if (orderId == null) {
            return null;
        }
        return orderChargeMapper.selectById(orderId);
    }

    @Override
    public DataGridView queryAllOrderChargeForPage(OrderChargeDto orderChargeDto) {
        QueryWrapper<OrderCharge> qw = new QueryWrapper<>();
        Page<OrderCharge> page = new Page<>();
        qw.like(StringUtils.isNotBlank(orderChargeDto.getPatientName()), OrderCharge.COL_PATIENT_NAME, orderChargeDto.getPatientName());
        qw.eq(StringUtils.isNotBlank(orderChargeDto.getRegId()), OrderCharge.COL_REG_ID, orderChargeDto.getRegId());
        qw.orderByDesc(OrderCharge.COL_CREATE_TIME);
        orderChargeMapper.selectPage(page, qw);
        return new DataGridView(page.getTotal(), page.getRecords());
    }

    @Override
    public List<OrderChargeItem> queryOrderChargeItemByOrderId(String orderId) {
        if (orderId != null) {
            QueryWrapper<OrderChargeItem> qw = new QueryWrapper<>();
            qw.eq(OrderChargeItem.COL_ORDER_ID, orderId);
            return orderChargeItemMapper.selectList(qw);
        }
        return null;
    }

    @Override
    public OrderChargeItem queryOrderChargeItemByItemId(String itemId) {
        if (itemId != null) {
            return orderChargeItemMapper.selectById(itemId);
        }
        return null;
    }
}
