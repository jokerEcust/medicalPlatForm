package com.ecust.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ecust.constants.Constants;
import com.ecust.dto.ProducterDto;
import com.ecust.vo.DataGridView;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Method;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

import com.ecust.domain.Producter;
import com.ecust.mapper.ProducterMapper;
import com.ecust.service.ProducterService;

@Service(methods = {@Method(name = "addProducter",retries = 0)})
public class ProducterServiceImpl implements ProducterService {
    @Autowired
    ProducterMapper producterMapper;

    @Override
    public DataGridView listForPage(ProducterDto producterDto) {
        QueryWrapper<Producter> queryWrapper = new QueryWrapper<>();
        Page<Producter> page = new Page<>(producterDto.getPageNum(), producterDto.getPageSize());
        queryWrapper.like(StringUtils.isNoneBlank(producterDto.getProducterName()), Producter.COL_PRODUCTER_NAME, producterDto.getProducterName());
        queryWrapper.like(StringUtils.isNoneBlank(producterDto.getKeywords()), Producter.COL_KEYWORDS, producterDto.getKeywords());
        queryWrapper.like(StringUtils.isNoneBlank(producterDto.getProducterTel()), Producter.COL_PRODUCTER_TEL, producterDto.getProducterTel());
        queryWrapper.eq(StringUtils.isNoneBlank(producterDto.getStatus()), Producter.COL_STATUS, producterDto.getStatus());
        queryWrapper.le(producterDto.getEndTime()!=null,Producter.COL_CREATE_TIME,producterDto.getEndTime());
        queryWrapper.ge(producterDto.getBeginTime()!=null,Producter.COL_CREATE_TIME,producterDto.getBeginTime());
        producterMapper.selectPage(page, queryWrapper);
        return new DataGridView(page.getTotal(), page.getRecords());
    }

    @Override
    public int addProducter(ProducterDto producterDto) {
        Producter item = new Producter();
        BeanUtil.copyProperties(producterDto, item);
        item.setCreateBy(producterDto.getSimpleUser().getUserName());
        item.setCreateTime(DateUtil.date());
        return producterMapper.insert(item);
    }

    @Override
    public Producter queryProducterById(Long producterId) {
        if (producterId != null) {
            return producterMapper.selectById(producterId);
        }
        return null;
    }

    @Override
    public int updateProducter(ProducterDto producterDto) {
        Producter item = new Producter();
        BeanUtil.copyProperties(producterDto, item);
        item.setUpdateBy(producterDto.getSimpleUser().getUserName());
        return producterMapper.updateById(item);
    }

    @Override
    public int deleteProducterByIds(Long[] producterIds) {
        if (producterIds != null && producterIds.length!=0) {
           return producterMapper.deleteBatchIds(Arrays.asList(producterIds));
        }
        return 0;
    }

    @Override
    public List<Producter> queryAllProducter() {
        QueryWrapper<Producter> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(Producter.COL_STATUS, Constants.STATUS_TRUE);
        return producterMapper.selectList(queryWrapper);
    }
}
