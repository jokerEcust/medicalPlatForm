package com.ecust.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ecust.constants.Constants;
import com.ecust.dto.RegisteredItemDto;
import com.ecust.vo.DataGridView;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecust.mapper.RegisteredItemMapper;
import com.ecust.domain.RegisteredItem;
import com.ecust.service.RegisteredItemService;
@Service
public class RegisteredItemServiceImpl implements RegisteredItemService{

    @Autowired
    RegisteredItemMapper registeredItemMapper;
    @Override
    public DataGridView listForPage(RegisteredItemDto registeredItemDto) {
        QueryWrapper<RegisteredItem> queryWrapper = new QueryWrapper<>();
        Page<RegisteredItem> page = new Page<>(registeredItemDto.getPageNum(),registeredItemDto.getPageSize());
        queryWrapper.like(StringUtils.isNoneBlank(registeredItemDto.getRegItemName()),RegisteredItem.COL_REG_ITEM_NAME,registeredItemDto.getRegItemName());
        queryWrapper.eq(StringUtils.isNoneBlank(registeredItemDto.getStatus()),RegisteredItem.COL_STATUS,registeredItemDto.getStatus());
        registeredItemMapper.selectPage(page,queryWrapper);
        return new DataGridView(page.getTotal(),page.getRecords());
    }

    @Override
    public int addRegisteredItem(RegisteredItemDto registeredItemDto) {
        RegisteredItem item = new RegisteredItem();
        BeanUtil.copyProperties(registeredItemDto,item);
        item.setCreateBy(registeredItemDto.getSimpleUser().getUserName());
        item.setCreateTime(DateUtil.date());
        return registeredItemMapper.insert(item);
    }

    @Override
    public RegisteredItem queryRegisteredItemById(Long registeredItemId) {
        if (registeredItemId!=null){
            return registeredItemMapper.selectById(registeredItemId);
        }
        return null;
    }

    @Override
    public int updateRegisteredItem(RegisteredItemDto registeredItemDto) {
        RegisteredItem item = new RegisteredItem();
        BeanUtil.copyProperties(registeredItemDto,item);
        item.setUpdateBy(registeredItemDto.getSimpleUser().getUserName());
        return registeredItemMapper.updateById(item);
    }

    @Override
    public int deleteRegisteredItemById(Long regItemId) {
        if (regItemId!=null){
            return registeredItemMapper.deleteById(regItemId);
        }
        return 0;
    }

    @Override
    public List<RegisteredItem> queryAllRegisteredItem() {
        QueryWrapper<RegisteredItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(RegisteredItem.COL_STATUS, Constants.STATUS_TRUE);
        return registeredItemMapper.selectList(queryWrapper);
    }
}
