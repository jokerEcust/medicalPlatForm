package com.ecust.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ecust.constants.Constants;
import com.ecust.domain.*;
import com.ecust.dto.CareHistoryDto;
import com.ecust.dto.CareOrderDto;
import com.ecust.dto.CareOrderFormDto;
import com.ecust.dto.CareOrderItemDto;
import com.ecust.mapper.*;
import com.ecust.service.CareService;
import com.ecust.service.MedicinesService;
import com.ecust.utils.IdGeneratorSnowflake;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class CareServiceImpl implements CareService {
    @Autowired
    CareHistoryMapper careHistoryMapper;

    @Autowired
    CareOrderMapper careOrderMapper;

    @Autowired
    CareOrderItemMapper careOrderItemMapper;

    @Autowired
    OrderChargeItemMapper orderChargeItemMapper;

    @Autowired
    RegistrationMapper registrationMapper;

    @Reference
    MedicinesService medicinesService;

    @Override
    public List<CareHistory> getCareHistoryByPatientId(String patientId) {
        if (patientId == null) {
            return null;
        }
        QueryWrapper<CareHistory> qw = new QueryWrapper<>();
        qw.eq(CareHistory.COL_PATIENT_ID, patientId);
        return careHistoryMapper.selectList(qw);
    }

    @Override
    public CareHistory saveOrUpdateCareHistory(CareHistoryDto careHistoryDto) {
        CareHistory careHistory = new CareHistory();
        BeanUtil.copyProperties(careHistoryDto, careHistory);
        if (StringUtils.isNotBlank(careHistory.getChId())) {
            this.careHistoryMapper.updateById(careHistory);
        } else {
            careHistory.setChId(IdGeneratorSnowflake.generatorIdWithProfix(Constants.ID_PROFIX_CH));
            careHistoryMapper.insert(careHistory);
        }
        return careHistory;
    }

    @Override
    public CareHistory queryCareHistoryByRegId(String regId) {
        if (regId != null) {
            QueryWrapper<CareHistory> qw = new QueryWrapper<>();
            qw.eq(CareHistory.COL_REG_ID, regId);
            return careHistoryMapper.selectOne(qw);
        }
        return null;
    }

    @Override
    public List<CareOrder> queryCareOrdersByChId(String chId) {
        if (StringUtils.isNotBlank(chId)) {
            QueryWrapper<CareOrder> qw = new QueryWrapper<>();
            qw.eq(CareOrder.COL_CH_ID, chId);
            return careOrderMapper.selectList(qw);
        }
        return Collections.EMPTY_LIST;
    }

    @Override
    public List<CareOrderItem> queryCareOrderItemsByCoId(String coId) {
        if (StringUtils.isNotBlank(coId)) {
            QueryWrapper<CareOrderItem> qw = new QueryWrapper<>();
            qw.eq(CareOrderItem.COL_CO_ID, coId);
            return careOrderItemMapper.selectList(qw);
        }
        return Collections.EMPTY_LIST;
    }

    @Override
    public List<CareOrderItem> queryCareOrderItemsByCoId(String coId, String status) {
        if (StringUtils.isNotBlank(coId)) {
            QueryWrapper<CareOrderItem> qw = new QueryWrapper<>();
            qw.eq(CareOrderItem.COL_CO_ID, coId);
            qw.eq(StringUtils.isNotBlank(status), CareOrderItem.COL_STATUS, status);
            return careOrderItemMapper.selectList(qw);
        }
        return Collections.EMPTY_LIST;
    }

    @Override
    public CareHistory queryCareHistoryByChId(String chId) {
        if (StringUtils.isNotBlank(chId)) {
            QueryWrapper<CareHistory> qw = new QueryWrapper<>();
            qw.eq(CareHistory.COL_CH_ID, chId);
            return careHistoryMapper.selectOne(qw);
        }
        return null;
    }

    @Override
    public int saveCareOrderItem(CareOrderFormDto careOrderFormDto) {
        //保存处方以及处方详情
        //保存处方
        CareOrderDto careOrderDto = careOrderFormDto.getCareOrder();
        CareOrder careOrder = new CareOrder();
        BeanUtil.copyProperties(careOrderDto, careOrder);
        careOrder.setCreateBy(careOrderDto.getSimpleUser().getUserName());
        careOrder.setCreateTime(DateUtil.date());
        int insert1 = careOrderMapper.insert(careOrder);
        List<CareOrderItemDto> careOrderItemDtos = careOrderFormDto.getCareOrderItems();
        int insert2 = 0;
        for (CareOrderItemDto careOrderItemDto : careOrderItemDtos) {
            CareOrderItem careOrderItem = new CareOrderItem();
            BeanUtil.copyProperties(careOrderItemDto, careOrderItem);
            careOrderItem.setItemId(IdGeneratorSnowflake.generatorIdWithProfix(Constants.ID_PROFIX_ITEM));
            careOrderItem.setCoId(careOrder.getCoId());
            careOrderItem.setStatus(Constants.ORDER_DETAILS_STATUS_0);
            careOrderItem.setCreateTime(DateUtil.date());
            insert2 = careOrderItemMapper.insert(careOrderItem);
        }
        if (insert1 == 1 && insert2 == 1) {
            return 1;
        }
        return 0;
    }

    @Override
    public CareOrderItem queryCareOrderItemByItemId(String itemId) {
        return careOrderItemMapper.selectById(itemId);
    }

    @Override
    public int deleteCareOrderItemByItemId(String itemId) {
        //删除条目，还需更新处方信息
        CareOrderItem careOrderItem = careOrderItemMapper.selectById(itemId);
        String coId = careOrderItem.getCoId();
        int i = careOrderItemMapper.deleteById(itemId);

        QueryWrapper<CareOrderItem> qw = new QueryWrapper<>();
        qw.eq(CareOrderItem.COL_CO_ID, coId);
        List<CareOrderItem> careOrderItems = careOrderItemMapper.selectList(qw);
        if (careOrderItems != null && careOrderItems.size() > 0) {
            //计算处方表中的价格
            BigDecimal allAmount = new BigDecimal("0");
            for (CareOrderItem orderItem : careOrderItems) {
                allAmount = allAmount.add(orderItem.getAmount());
            }
            CareOrder careOrder = careOrderMapper.selectById(coId);
            careOrder.setAllAmount(allAmount);
            careOrderMapper.updateById(careOrder);
        } else {
            //没有详情信息，即处方下没有东西，直接删除处方表的数据
            careOrderMapper.deleteById(coId);
        }
        return i;
    }

    @Override
    public int visitComplete(String regId) {
        Registration registration = new Registration();
        registration.setRegId(regId);
        registration.setRegStatus(Constants.REG_STATUS_3);
        return this.registrationMapper.updateById(registration);
    }

    @Override
    public String doMedicine(List<String> itemIds) {
        //药品足够，则更新数据库，不够则给予提示，并不修改数据库
        QueryWrapper<CareOrderItem> qw = new QueryWrapper<>();
        qw.in(CareOrderItem.COL_ITEM_ID, itemIds);
        List<CareOrderItem> careOrderItems = careOrderItemMapper.selectList(qw);
        StringBuffer sb = new StringBuffer();
        for (CareOrderItem careOrderItem : careOrderItems) {
            //库存扣减
            int i = medicinesService.deductionMedicinesStorage(Long.valueOf(careOrderItem.getItemRefId()), careOrderItem.getNum().longValue());
            if (i >= 0) {
                //库存够
//               更新处方详情状态
                careOrderItem.setStatus(Constants.ORDER_DETAILS_STATUS_3);//已完成
                this.careOrderItemMapper.updateById(careOrderItem);

                //更新收费详情状态
                OrderChargeItem orderChargeItem=new OrderChargeItem();
                orderChargeItem.setItemId(careOrderItem.getItemId());
                orderChargeItem.setStatus(Constants.ORDER_DETAILS_STATUS_3);
                this.orderChargeItemMapper.updateById(orderChargeItem);
            }else {
                sb.append("【"+careOrderItem.getItemName()+"】发药失败\n");
            }
        }
        if (StringUtils.isNotBlank(sb.toString())){
            sb.append("原因：库存不足");
            return sb.toString();
        }
        return null;
    }

    @Override
    public List<CareOrderItem> queryCareOrderItemsByStatus(String coType, String status) {
        QueryWrapper<CareOrderItem> qw=new QueryWrapper<>();
        qw.eq(StringUtils.isNotBlank(coType),CareOrderItem.COL_ITEM_TYPE,coType);
        qw.eq(StringUtils.isNotBlank(status),CareOrderItem.COL_STATUS,status);
        return this.careOrderItemMapper.selectList(qw);
    }

    @Override
    public CareOrder queryCareOrderByCoId(String coId) {
        return this.careOrderMapper.selectById(coId);
    }

}
