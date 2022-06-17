package com.ecust.service.impl;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ecust.constants.Constants;
import com.ecust.domain.*;
import com.ecust.domain.OrderBackfee;
import com.ecust.dto.OrderBackfeeDto;
import com.ecust.dto.OrderBackfeeFormDto;
import com.ecust.dto.OrderBackfeeItemDto;
import com.ecust.mapper.*;
import com.ecust.service.OrderBackfeeService;

import com.ecust.vo.DataGridView;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderBackfeeServiceImpl implements OrderBackfeeService {
    @Autowired
    OrderBackfeeMapper orderBackfeeMapper;
    @Autowired
    OrderBackfeeItemMapper orderBackfeeItemMapper;
    @Autowired
    OrderChargeItemMapper orderChargeItemMapper;
    @Autowired
    CareOrderItemMapper careOrderItemMapper;
    @Override
    public void saveOrderAndItems(OrderBackfeeFormDto orderBackfeeFormDto) {
        OrderBackfeeDto orderBackfeeDto = orderBackfeeFormDto.getOrderBackfeeDto();
        List<OrderBackfeeItemDto> orderBackfeeItemDtoList = orderBackfeeFormDto.getOrderBackfeeItemDtoList();

        OrderBackfee orderBackfee = new OrderBackfee();
        BeanUtil.copyProperties(orderBackfeeDto, orderBackfee);
        orderBackfee.setBackStatus(Constants.ORDER_STATUS_0);
        orderBackfee.setCreateBy(orderBackfeeFormDto.getSimpleUser().getUserName());
        orderBackfee.setCreateTime(DateUtil.date());
        int i = orderBackfeeMapper.insert(orderBackfee);
        for (OrderBackfeeItemDto orderBackfeeItemDto : orderBackfeeItemDtoList) {
            OrderBackfeeItem orderBackfeeItem = new OrderBackfeeItem();
            BeanUtil.copyProperties(orderBackfeeItemDto, orderBackfeeItem);
            orderBackfeeItem.setBackId(orderBackfee.getBackId());
            orderBackfeeItem.setStatus(Constants.ORDER_DETAILS_STATUS_0);
            orderBackfeeItemMapper.insert(orderBackfeeItem);
        }
    }

    @Override
    public void backSuccess(String backId, String backPlatformId, String backType) {
        System.out.println("================================");
        //根据退费订单 ID 查询退费订单
        OrderBackfee orderBackfee = this.orderBackfeeMapper.selectById(backId);
        //设置平台交易编号
        orderBackfee.setBackPlatformId(backPlatformId);
        //设置退费类型
        orderBackfee.setBackType(backType);
        //设置退费时间
        orderBackfee.setBackTime(DateUtil.date());
//修改订单状态
        orderBackfee.setBackStatus(Constants.ORDER_BACKFEE_STATUS_1);//已退费
//更新订单状态
        this.orderBackfeeMapper.updateById(orderBackfee);
//根据退费订单号查询退费订单详情
        QueryWrapper<OrderBackfeeItem> qw=new QueryWrapper<>();
        qw.eq(OrderBackfeeItem.COL_BACK_ID,backId);
        List<OrderBackfeeItem> orderBackfeeItems = this.orderBackfeeItemMapper.selectList(qw);
        List<String> allItemIds=new ArrayList<>();
        for (OrderBackfeeItem orderBackfeeItem : orderBackfeeItems) {
            allItemIds.add(orderBackfeeItem.getItemId());
        }
        System.out.println("allItems: "+allItemIds);
//更新退费单的详情状态
        OrderBackfeeItem orderBackItemObj=new OrderBackfeeItem();
        orderBackItemObj.setStatus(Constants.ORDER_DETAILS_STATUS_2);//已退费
        QueryWrapper<OrderBackfeeItem> orderBackItemQw=new QueryWrapper<>();
        orderBackItemQw.in(OrderBackfeeItem.COL_ITEM_ID,allItemIds);
        this.orderBackfeeItemMapper.update(orderBackItemObj,orderBackItemQw);
//更新收费详情的状态
        OrderChargeItem orderItemObj=new OrderChargeItem();
        orderItemObj.setStatus(Constants.ORDER_DETAILS_STATUS_2);//已退费
        QueryWrapper<OrderChargeItem> orderItemQw=new QueryWrapper<>();
        orderItemQw.in(OrderChargeItem.COL_ITEM_ID,allItemIds);
        this.orderChargeItemMapper.update(orderItemObj,orderItemQw);
//更新处方详情的状态
        CareOrderItem careItemObj=new CareOrderItem();
        careItemObj.setStatus(Constants.ORDER_DETAILS_STATUS_2);//已退费
        QueryWrapper<CareOrderItem> careItemQw=new QueryWrapper<>();
        careItemQw.in(CareOrderItem.COL_ITEM_ID,allItemIds);
        this.careOrderItemMapper.update(careItemObj,careItemQw);
    }

    @Override
    public DataGridView queryOrderBackfeeForPage(OrderBackfeeDto orderBackfeeDto) {
        QueryWrapper<OrderBackfee> qw=new QueryWrapper<>();
        Page<OrderBackfee> page=new Page<>();
        qw.like(StringUtils.isNotBlank(orderBackfeeDto.getPatientName()),OrderCharge.COL_PATIENT_NAME,orderBackfeeDto.getPatientName());
        qw.eq(StringUtils.isNotBlank(orderBackfeeDto.getRegId()),OrderCharge.COL_REG_ID,orderBackfeeDto.getRegId());
        qw.orderByDesc(OrderBackfee.COL_CREATE_TIME);
        orderBackfeeMapper.selectPage(page,qw);
        return new DataGridView(page.getTotal(),page.getRecords());
    }

    @Override
    public OrderBackfeeItem queryOrderBackfeeItemByBackId(String backId) {
        if (backId==null){
            return null;
        }
        QueryWrapper<OrderBackfeeItem> qw=new QueryWrapper<>();
        qw.eq(OrderBackfeeItem.COL_BACK_ID,backId);
        return orderBackfeeItemMapper.selectOne(qw);
    }
}
