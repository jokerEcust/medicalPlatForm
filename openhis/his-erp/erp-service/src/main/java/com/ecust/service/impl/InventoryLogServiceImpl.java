package com.ecust.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ecust.domain.InventoryLog;
import com.ecust.dto.InventoryLogDto;
import com.ecust.mapper.InventoryLogMapper;
import com.ecust.service.InventoryLogService;
import com.ecust.vo.DataGridView;
 import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;


@Service
public class InventoryLogServiceImpl implements InventoryLogService {
    @Autowired
    InventoryLogMapper inventoryLogMapper;
    @Override
    public DataGridView listForPage(InventoryLogDto inventoryLogDto) {
        QueryWrapper<InventoryLog> qw=new QueryWrapper<>();
        Page<InventoryLog> page=new Page<>(inventoryLogDto.getPageNum(),inventoryLogDto.getPageSize());
        qw.eq(StringUtils.isNoneBlank(inventoryLogDto.getPurchaseId()), InventoryLog.COL_INVENTORY_LOG_ID,inventoryLogDto.getPurchaseId());
        qw.like(StringUtils.isNoneBlank(inventoryLogDto.getMedicinesName()),InventoryLog.COL_MEDICINES_NAME,inventoryLogDto.getMedicinesName());
        qw.eq(StringUtils.isNoneBlank(inventoryLogDto.getMedicinesType()), InventoryLog.COL_MEDICINES_TYPE,inventoryLogDto.getMedicinesType());
        qw.eq(StringUtils.isNoneBlank(inventoryLogDto.getPrescriptionType()), InventoryLog.COL_PRESCRIPTION_TYPE,inventoryLogDto.getPrescriptionType());
        qw.like(StringUtils.isNoneBlank(inventoryLogDto.getProducterId()),InventoryLog.COL_PRODUCTER_ID,inventoryLogDto.getProducterId());
        qw.le(inventoryLogDto.getEndTime()!=null,InventoryLog.COL_CREATE_TIME,inventoryLogDto.getEndTime());
        qw.ge(inventoryLogDto.getBeginTime()!=null,InventoryLog.COL_CREATE_TIME,inventoryLogDto.getBeginTime());
        inventoryLogMapper.selectPage(page,qw);
        return new DataGridView(page.getTotal(),page.getRecords());
    }
}
