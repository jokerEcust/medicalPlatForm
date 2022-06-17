package com.ecust.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ecust.constants.Constants;
import com.ecust.dto.MedicinesDto;
import com.ecust.vo.DataGridView;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;
import com.ecust.mapper.MedicinesMapper;
import com.ecust.domain.Medicines;
import com.ecust.service.MedicinesService;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MedicinesServiceImpl implements MedicinesService {

    @Autowired
    MedicinesMapper medicinesMapper;

    @Override
    public DataGridView listForPage(MedicinesDto medicinesDto) {
        Page<Medicines> page = new Page<>(medicinesDto.getPageNum(), medicinesDto.getPageSize());
        QueryWrapper<Medicines> qw = new QueryWrapper();
        qw.like(StringUtils.isNoneBlank(medicinesDto.getMedicinesName()), Medicines.COL_MEDICINES_NAME, medicinesDto.getMedicinesName());
        qw.like(StringUtils.isNoneBlank(medicinesDto.getKeywords()), Medicines.COL_KEYWORDS, medicinesDto.getKeywords());
        qw.eq(StringUtils.isNoneBlank(medicinesDto.getPrescriptionType()), Medicines.COL_PRESCRIPTION_TYPE, medicinesDto.getPrescriptionType());
        qw.eq(StringUtils.isNotBlank(medicinesDto.getProducterId()), Medicines.COL_PRODUCTER_ID, medicinesDto.getProducterId());
        qw.eq(StringUtils.isNotBlank(medicinesDto.getPrescriptionType()), Medicines.COL_PRESCRIPTION_TYPE, medicinesDto.getPrescriptionType());
        qw.eq(StringUtils.isNotBlank(medicinesDto.getStatus()), Medicines.COL_STATUS, medicinesDto.getStatus());
        medicinesMapper.selectPage(page, qw);
        return new DataGridView(page.getTotal(), page.getRecords());
    }

    @Override
    public int addMedicines(MedicinesDto medicinesDto) {
        Medicines medicines = new Medicines();
        BeanUtil.copyProperties(medicinesDto, medicines);
        medicines.setCreateTime(DateUtil.date());
        medicines.setCreateBy(medicinesDto.getSimpleUser().getUserName());
        return this.medicinesMapper.insert(medicines);
    }

    @Override
    public Medicines queryMedicinesById(Long medicinesId) {
        return this.medicinesMapper.selectById(medicinesId);
    }

    @Override
    public int updateMedicines(MedicinesDto medicinesDto) {
        Medicines medicines = new Medicines();
        BeanUtil.copyProperties(medicinesDto, medicines);
        medicines.setUpdateBy(medicinesDto.getSimpleUser().getUserName());
        return this.medicinesMapper.updateById(medicines);
    }

    @Override
    public int deleteMedicinesByIds(Long[] medicinesIds) {
        List<Long> ids = Arrays.asList(medicinesIds);
        if (ids.size() > 0) {
            return this.medicinesMapper.deleteBatchIds(ids);
        }
        return 0;
    }

    @Override
    public List<Medicines> queryAllMedicines() {
        QueryWrapper<Medicines> qw=new QueryWrapper<>();
        qw.eq(Medicines.COL_STATUS, Constants.STATUS_TRUE);
        return medicinesMapper.selectList(qw);
    }

    @Override
    public int updateMedicinesStorage(Long medicinesId, Long medicinesStockNum) {
        Medicines medicines = new Medicines();
        medicines.setMedicinesId(medicinesId);
        medicines.setMedicinesStockNum(medicinesStockNum);
        return this.medicinesMapper.updateById(medicines);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deductionMedicinesStorage(Long medicinesId, Long num) {
        Medicines medicines = medicinesMapper.selectById(medicinesId);
//        return medicines.getMedicinesStockNum().compareTo(num);
        if (medicines.getMedicinesStockNum().compareTo(num)<0){
            return -1;
        }
        medicines.setMedicinesStockNum(medicines.getMedicinesStockNum()-num);
        medicinesMapper.updateById(medicines);
        return 1;
    }
}
