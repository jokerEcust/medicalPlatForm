package com.ecust.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ecust.constants.Constants;
import com.ecust.dto.CheckItemDto;
import com.ecust.vo.DataGridView;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecust.mapper.CheckItemMapper;
import com.ecust.domain.CheckItem;
import com.ecust.service.CheckItemService;

@Service
public class CheckItemServiceImpl implements CheckItemService {
    @Autowired
    CheckItemMapper checkItemMapper;

    @Override
    public DataGridView listForPage(CheckItemDto checkItemDto) {
        QueryWrapper<CheckItem> qw=new QueryWrapper<>();
        Page<CheckItem> page=new Page<>(checkItemDto.getPageNum(),checkItemDto.getPageSize());
        qw.like(StringUtils.isNoneBlank(checkItemDto.getCheckItemName()),CheckItem.COL_CHECK_ITEM_NAME,checkItemDto.getCheckItemName());
        qw.like(StringUtils.isNoneBlank(checkItemDto.getKeywords()),CheckItem.COL_KEYWORDS,checkItemDto.getKeywords());
        qw.eq(StringUtils.isNoneBlank(checkItemDto.getStatus()),CheckItem.COL_STATUS,checkItemDto.getStatus());
        qw.eq(StringUtils.isNoneBlank(checkItemDto.getTypeId()),CheckItem.COL_TYPE_ID,checkItemDto.getTypeId());
        checkItemMapper.selectPage(page,qw);
        return new DataGridView(page.getTotal(),page.getRecords());
    }

    @Override
    public int addCheckItem(CheckItemDto checkItemDto) {
        CheckItem checkItem = new CheckItem();
        checkItem.setCreateBy(checkItemDto.getSimpleUser().getUserName());
        checkItem.setCreateTime(DateUtil.date());
        BeanUtil.copyProperties(checkItemDto,checkItem);
        return checkItemMapper.insert(checkItem);
    }

    @Override
    public CheckItem queryCheckItemById(Long checkItemId) {
        if (checkItemId==null){
            return null;
        }
        return checkItemMapper.selectById(checkItemId);
    }

    @Override
    public int updateCheckItem(CheckItemDto checkItemDto) {
        CheckItem checkItem = new CheckItem();
        checkItem.setUpdateBy(checkItemDto.getSimpleUser().getUserName());
        BeanUtil.copyProperties(checkItemDto,checkItem);
        return checkItemMapper.updateById(checkItem);
    }

    @Override
    public int deleteCheckItemById(Long checkItemId) {
        if (checkItemId==null){
            return 0;
        }
        return checkItemMapper.deleteById(checkItemId);
    }

    @Override
    public List<CheckItem> queryAllCheckItem() {
        QueryWrapper<CheckItem> qw=new QueryWrapper<>();
        qw.eq(CheckItem.COL_STATUS, Constants.STATUS_TRUE);
        return checkItemMapper.selectList(qw);
    }
}
