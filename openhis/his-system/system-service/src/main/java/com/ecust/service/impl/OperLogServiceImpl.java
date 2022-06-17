package com.ecust.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ecust.dto.OperLogDto;
import com.ecust.vo.DataGridView;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecust.mapper.OperLogMapper;
import com.ecust.domain.OperLog;
import com.ecust.service.OperLogService;

@Service
public class OperLogServiceImpl implements OperLogService {
    @Autowired
    OperLogMapper operLogMapper;

    @Override
    public DataGridView listForPage(OperLogDto operLogDto) {
        QueryWrapper<OperLog> qw = new QueryWrapper<>();
        Page<OperLog> page = new Page<>(operLogDto.getPageNum(), operLogDto.getPageSize());
        //操作名称、标题、BUSINESS_TYPE、状态、操作时间（区间）
        qw.like(StringUtils.isNoneBlank(operLogDto.getOperName()), OperLog.COL_OPER_NAME, operLogDto.getOperName());
        qw.like(StringUtils.isNoneBlank(operLogDto.getTitle()), OperLog.COL_TITLE, operLogDto.getTitle());
        qw.eq(StringUtils.isNoneBlank(operLogDto.getBusinessType()), OperLog.COL_BUSINESS_TYPE, operLogDto.getBusinessType());
        qw.eq(StringUtils.isNoneBlank(operLogDto.getStatus()), OperLog.COL_STATUS, operLogDto.getStatus());
        qw.ge(null != operLogDto.getBeginTime(), OperLog.COL_OPER_TIME, operLogDto.getBeginTime());
        qw.le(null != operLogDto.getEndTime(), OperLog.COL_OPER_TIME, operLogDto.getEndTime());
        qw.orderByDesc(OperLog.COL_OPER_TIME);
        operLogMapper.selectPage(page, qw);
        return new DataGridView(page.getTotal(), page.getRecords());
    }

    @Override
    public int deleteOperLogByIds(Long[] infoIds) {
        if(infoIds==null||infoIds.length==0){
            return 0;
        }
        List<Long> ids = Arrays.asList(infoIds);
        return operLogMapper.deleteBatchIds(ids);

    }

    @Override
    public int clearAllOperLog() {
        return operLogMapper.delete(null);
    }

    @Override
    public void insertOperLog(OperLog operLog) {
        operLogMapper.insert(operLog);
    }
}
