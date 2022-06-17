package com.ecust.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ecust.constants.Constants;
import com.ecust.domain.Provider;
import com.ecust.dto.ProviderDto;
import com.ecust.mapper.ProviderMapper;
import com.ecust.vo.DataGridView;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import com.ecust.service.ProviderService;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Method;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service(methods = {@Method(name = "addProvider", retries = 0)})
public class ProviderServiceImpl implements ProviderService {
    @Autowired
    ProviderMapper providerMapper;

    @Override
    public DataGridView listForPage(ProviderDto providerDto) {
        QueryWrapper<Provider> queryWrapper = new QueryWrapper<>();
        Page<Provider> page = new Page(providerDto.getPageNum(), providerDto.getPageSize());
        queryWrapper.like(StringUtils.isNoneBlank(providerDto.getProviderName()), Provider.COL_PROVIDER_NAME, providerDto.getProviderName());
        queryWrapper.like(StringUtils.isNoneBlank(providerDto.getContactName()), Provider.COL_CONTACT_NAME, providerDto.getContactName());
        queryWrapper.and(StringUtils.isNotBlank(providerDto.getContactTel()), new Consumer<QueryWrapper<Provider>>() {
                    @Override //(tel like ? or mobile like ?)
                    public void accept(QueryWrapper<Provider> providerQueryWrapper) {
                        providerQueryWrapper.like(Provider.COL_CONTACT_TEL, providerDto.getContactTel())
                                .or().like(Provider.COL_CONTACT_MOBILE, providerDto.getContactTel());
                    }
                });
        queryWrapper.eq(StringUtils.isNoneBlank(providerDto.getStatus()), Provider.COL_STATUS, providerDto.getStatus());
        providerMapper.selectPage(page, queryWrapper);
        return new DataGridView(page.getTotal(), page.getRecords());
    }

    @Override
    public int addProvider(ProviderDto providerDto) {
        Provider item = new Provider();
        BeanUtil.copyProperties(providerDto, item);
        item.setCreateBy(providerDto.getSimpleUser().getUserName());
        item.setCreateTime(DateUtil.date());
        return providerMapper.insert(item);
    }

    @Override
    public Provider queryProviderById(Long providerId) {
        if (providerId != null) {
            return providerMapper.selectById(providerId);
        }
        return null;
    }

    @Override
    public int updateProvider(ProviderDto providerDto) {
        Provider item = new Provider();
        BeanUtil.copyProperties(providerDto, item);
        item.setUpdateBy(providerDto.getSimpleUser().getUserName());
        return providerMapper.updateById(item);
    }

    @Override
    public int deleteProviderByIds(Long[] providerIds) {
        if (providerIds != null && providerIds.length != 0) {
            return providerMapper.deleteBatchIds(Arrays.asList(providerIds));
        }
        return 0;
    }

    @Override
    public List<Provider> queryAllProvider() {
        QueryWrapper<Provider> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(Provider.COL_STATUS, Constants.STATUS_TRUE);
        return providerMapper.selectList(queryWrapper);
    }
}
