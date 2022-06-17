package com.ecust.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ecust.constants.Constants;
import com.ecust.dto.DictDataDto;
import com.ecust.mapper.DictDataMapper;
import com.ecust.vo.DataGridView;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

import com.ecust.domain.DictData;
import com.ecust.service.DictDataService;

@Service
public class DictDataServiceImpl implements DictDataService {

    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Autowired
    DictDataMapper dictDataMapper;
    @Override
    public DataGridView listPage(DictDataDto dictDataDto) {
        Page<DictData> page=new Page(dictDataDto.getPageNum(),dictDataDto.getPageSize());
        QueryWrapper<DictData> qw = new QueryWrapper<>();
        //传入的是dictType,dictLabel,status
        qw.eq(StringUtils.isNoneBlank(dictDataDto.getDictType()),DictData.COL_DICT_TYPE,dictDataDto.getDictType());
        qw.like(StringUtils.isNoneBlank(dictDataDto.getDictLabel()),DictData.COL_DICT_LABEL,dictDataDto.getDictLabel());
        qw.eq(StringUtils.isNoneBlank(dictDataDto.getStatus()),DictData.COL_STATUS,dictDataDto.getStatus());
        dictDataMapper.selectPage(page,qw);
        return new DataGridView(page.getTotal(),page.getRecords());

    }

    @Override
    public int insert(DictDataDto dictDataDto) {
        DictData dictData = new DictData();
        BeanUtil.copyProperties(dictDataDto,dictData);
        dictData.setCreateBy(dictDataDto.getSimpleUser().getUserName());
        dictData.setCreateTime(DateUtil.date());
        return dictDataMapper.insert(dictData);
    }

    @Override
    public DictData selectDictDataById(Long dictCode) {
        return dictDataMapper.selectById(dictCode);
    }

    @Override
    public int update(DictDataDto dictDataDto) {
        DictData dictData = new DictData();
        BeanUtil.copyProperties(dictDataDto,dictData);
        dictData.setUpdateBy(dictDataDto.getSimpleUser().getUserName());
        return dictDataMapper.updateById(dictData);
    }

    @Override
    public int deleteDictDataByIds(Long[] dictCodeIds) {
        List<Long> ids = Arrays.asList(dictCodeIds);
        if(ids!=null && ids.size()>0){
            return dictDataMapper.deleteBatchIds(ids);
        }
        return -1;
    }

    @Override
    public List<DictData> selectDictDataByDictType(String dictType) {
        QueryWrapper<DictData> qw=new QueryWrapper<>();
        qw.eq(DictData.COL_DICT_TYPE,dictType);
        qw.eq(DictData.COL_STATUS, Constants.STATUS_TRUE);
        return dictDataMapper.selectList(qw);
        //这里利用redis做数据缓存，不需要走MySQL
//        String key=Constants.DICT_REDIS_PROFIX+dictType;
//        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
//        String json = ops.get(key);
//        List<DictData> dictData = JSON.parseArray(json, DictData.class);
//        return dictData;
    }
}
