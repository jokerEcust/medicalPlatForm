package com.ecust.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ecust.constants.Constants;
import com.ecust.domain.*;
import com.ecust.dto.PurchaseDto;
import com.ecust.dto.PurchaseFormDto;
import com.ecust.dto.PurchaseItemDto;
import com.ecust.mapper.InventoryLogMapper;
import com.ecust.mapper.MedicinesMapper;
import com.ecust.mapper.PurchaseItemMapper;
import com.ecust.utils.IdGeneratorSnowflake;
import com.ecust.vo.DataGridView;
import com.sun.org.apache.bcel.internal.Const;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;

import com.ecust.mapper.PurchaseMapper;
import com.ecust.service.PurchaseService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PurchaseServiceImpl implements PurchaseService {
    @Autowired
    PurchaseMapper purchaseMapper;

    @Autowired
    PurchaseItemMapper purchaseItemMapper;

    @Autowired
    InventoryLogMapper inventoryLogMapper;

    @Autowired
    MedicinesMapper medicinesMapper;

    @Override
    public DataGridView listForPage(PurchaseDto purchaseDto) {
        System.out.println("PurchaseServiceImpl.listForPage");
        QueryWrapper<Purchase> qw = new QueryWrapper<>();
        Page<Purchase> page = new Page<>(purchaseDto.getPageNum(), purchaseDto.getPageSize());
        qw.like(StringUtils.isNoneBlank(purchaseDto.getApplyUserName()), Purchase.COL_APPLY_USER_NAME, purchaseDto.getApplyUserName());
        qw.eq(StringUtils.isNoneBlank(purchaseDto.getStatus()), Purchase.COL_STATUS, purchaseDto.getStatus());
        purchaseMapper.selectPage(page, qw);
        return new DataGridView(page.getTotal(), page.getRecords());
    }

    /**
     * 提交审核
     * 未提交、审核失败->待审核
     * 1、4--->2
     *
     * @param purchaseId
     * @return
     */
    @Override
    public int doAudit(String purchaseId, SimpleUser simpleUser) {
        Purchase purchase = purchaseMapper.selectById(purchaseId);
        purchase.setStatus(Constants.STOCK_PURCHASE_STATUS_2);
        purchase.setApplyUserName(simpleUser.getUserName());
        purchase.setApplyUserId(Long.valueOf(simpleUser.getUserId().toString()));
        return purchaseMapper.updateById(purchase);
    }

    @Override
    public int doInvalid(String purchaseId) {
        Purchase purchase = this.purchaseMapper.selectById(purchaseId);
        purchase.setStatus(Constants.STOCK_PURCHASE_STATUS_5);//设置状态为作废
        return this.purchaseMapper.updateById(purchase);
    }

    @Override
    public int auditPass(String purchaseId) {
        Purchase purchase = this.purchaseMapper.selectById(purchaseId);
        purchase.setStatus(Constants.STOCK_PURCHASE_STATUS_3);//设置状态为审核通过
        return this.purchaseMapper.updateById(purchase);
    }

    @Override
    public int auditNoPass(String purchaseId, String auditMsg) {
        Purchase purchase = this.purchaseMapper.selectById(purchaseId);
        purchase.setStatus(Constants.STOCK_PURCHASE_STATUS_4);//设置状态为审核通过
        purchase.setAuditMsg(auditMsg);
        return this.purchaseMapper.updateById(purchase);
    }

    @Override
    public Purchase getPurchaseById(String purchaseId) {
        return purchaseMapper.selectById(purchaseId);
    }

    @Override
    public List<PurchaseItem> getPurchaseItemById(String purchaseId) {
        if(purchaseId!=null){
            QueryWrapper<PurchaseItem> qw=new QueryWrapper<>();
            qw.eq(PurchaseItem.COL_PURCHASE_ID,purchaseId);
            return purchaseItemMapper.selectList(qw);
        }
        return Collections.EMPTY_LIST;

    }

    @Override
    public int addPurchaseAndItem(PurchaseFormDto purchaseFormDto) {
        //暂存业务：需要先判断数据是否存在，存在：修改；不存在：增添
        String purchaseId = purchaseFormDto.getPurchaseDto().getPurchaseId();
        Purchase purchase = this.getPurchaseById(purchaseId);
        if (purchase != null) {
            purchaseMapper.deleteById(purchaseId);
            QueryWrapper<PurchaseItem> qw = new QueryWrapper<>();
            qw.eq(PurchaseItem.COL_PURCHASE_ID, purchaseId);
            purchaseItemMapper.delete(qw);
        }
        Purchase purchase1 = new Purchase();
        BeanUtil.copyProperties(purchaseFormDto.getPurchaseDto(), purchase1);
        purchase1.setStatus(Constants.STOCK_PURCHASE_STATUS_1);//设置未提交状态
        purchase1.setCreateBy(purchaseFormDto.getPurchaseDto().getSimpleUser().getUserName());
        for (PurchaseItemDto item :
                purchaseFormDto.getPurchaseItemDtos()) {
            PurchaseItem purchaseItem=new PurchaseItem();
            BeanUtil.copyProperties(item,purchaseItem);
            purchaseItem.setPurchaseId(purchaseId);
            purchaseItem.setItemId(IdGeneratorSnowflake.snowflakeId().toString());
            purchaseItemMapper.insert(purchaseItem);
        }
        return purchaseMapper.insert(purchase1);
    }

    /**
     * 提交审核
     * @param purchaseFormDto
     * @return
     */
    @Override
    public int addPurchaseAndItemToAudit(PurchaseFormDto purchaseFormDto) {
        String purchaseId = purchaseFormDto.getPurchaseDto().getPurchaseId();
        Purchase purchase = this.getPurchaseById(purchaseId);
        if (purchase != null) {
            purchaseMapper.deleteById(purchaseId);
            QueryWrapper<PurchaseItem> qw = new QueryWrapper<>();
            qw.eq(PurchaseItem.COL_PURCHASE_ID, purchaseId);
            purchaseItemMapper.delete(qw);
        }
        Purchase purchase1 = new Purchase();
        BeanUtil.copyProperties(purchaseFormDto.getPurchaseDto(), purchase1);
        purchase1.setStatus(Constants.STOCK_PURCHASE_STATUS_2);//设置为待审核状态
        purchase1.setCreateBy(purchaseFormDto.getPurchaseDto().getSimpleUser().getUserName());
        for (PurchaseItemDto item :
                purchaseFormDto.getPurchaseItemDtos()) {
            PurchaseItem purchaseItem=new PurchaseItem();
            BeanUtil.copyProperties(item,purchaseItem);
            purchaseItem.setPurchaseId(purchaseId);
            purchaseItem.setItemId(IdGeneratorSnowflake.snowflakeId().toString());
            purchaseItemMapper.insert(purchaseItem);
        }
        return purchaseMapper.insert(purchase1);
    }

    /**
     * 入库操作
     * 操作对象：stock_inventory_log、stock_medicines
     * @param purchaseId
     * @param simpleUser
     * @return
     */
    @Override
    public int doInventory(String purchaseId, SimpleUser simpleUser) {
        Purchase purchase = purchaseMapper.selectById(purchaseId);
        if(purchase!=null && purchase.getStatus().equals(Constants.STOCK_PURCHASE_STATUS_3)){
            //如果不为空并且状态为审核通过的情况下，才可以更新库存
            List<PurchaseItem> items = this.getPurchaseItemById(purchaseId);
            for (PurchaseItem item:items){
                InventoryLog log = new InventoryLog();
                log.setInventoryLogId(item.getItemId());
                log.setPurchaseId(purchaseId);
                log.setMedicinesId(item.getMedicinesId());
                log.setInventoryLogNum(item.getPurchaseNumber());
                log.setTradePrice(item.getTradePrice());
                log.setTradeTotalAmount(item.getTradeTotalAmount());
                log.setBatchNumber(item.getBatchNumber());
                log.setMedicinesName(item.getMedicinesName());
                log.setMedicinesType(item.getMedicinesType());
                log.setPrescriptionType(item.getPrescriptionType());
                log.setProducterId(item.getProducterId());
                log.setConversion(item.getConversion());
                log.setUnit(item.getUnit());
                log.setCreateTime(DateUtil.date());
                log.setCreateBy(simpleUser.getUserName());

                inventoryLogMapper.insert(log);

                Medicines medicines = medicinesMapper.selectById(item.getMedicinesId());
                medicines.setMedicinesStockNum(medicines.getMedicinesStockNum()+item.getPurchaseNumber());
                medicines.setUpdateBy(simpleUser.getUserName());
                medicinesMapper.updateById(medicines);

            }
            purchase.setStatus(Constants.STOCK_PURCHASE_STATUS_6);
            purchase.setStorageOptTime(DateUtil.date());
            purchase.setStorageOptUser(simpleUser.getUserName());
            return this.purchaseMapper.updateById(purchase);
        }
        return -1;
    }
}
