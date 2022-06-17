package com.ecust.service.impl;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ecust.constants.Constants;
import com.ecust.domain.DictData;
import com.ecust.dto.DictTypeDto;
import com.ecust.mapper.DictDataMapper;
import com.ecust.mapper.DictTypeMapper;
import com.ecust.vo.DataGridView;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import com.ecust.domain.DictType;
import com.ecust.service.DictTypeService;

import java.util.Arrays;
import java.util.List;

@Service
public class DictTypeServiceImpl implements DictTypeService{
    @Autowired
    DictTypeMapper dictTypeMapper;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    private DictDataMapper dictDataMapper;
    @Override
    public DataGridView listPage(DictTypeDto dictTypeDto) {
        Page<DictType> page=new Page<>(dictTypeDto.getPageNum(),dictTypeDto.getPageSize());
        QueryWrapper<DictType> qw=new QueryWrapper<>();
        //这里的字典类型名称与类型采用的是模糊匹配（没搞懂），个人觉得全等也可以做，更好理解
        //条件：名称、类型、日期区间、数据状态
        qw.like(StringUtils.isNoneBlank(dictTypeDto.getDictName()),DictType.COL_DICT_NAME,dictTypeDto.getDictName());//三参数的：条件、字段、值
        qw.like(StringUtils.isNoneBlank(dictTypeDto.getDictType()),DictType.COL_DICT_TYPE,dictTypeDto.getDictType());//三参数的：条件、字段、值
        qw.eq(StringUtils.isNoneBlank(dictTypeDto.getStatus()),DictType.COL_STATUS,dictTypeDto.getStatus());
        qw.between(dictTypeDto.getBeginTime()!=null && dictTypeDto.getEndTime()!=null,DictType.COL_CREATE_TIME,dictTypeDto.getBeginTime(),dictTypeDto.getEndTime());
        dictTypeMapper.selectPage(page,qw);
        return new DataGridView(page.getTotal(),page.getRecords());
    }

    @Override
    public int insert(DictTypeDto dictTypeDto) {
        //虽然dictTypeDto的数据类型与dictType一致，但是传进来的dictTypeDto一般没有创建时间、创建对象的值
        DictType dictType = new DictType();
        BeanUtil.copyProperties(dictTypeDto,dictType);
        dictType.setCreateTime(DateUtil.date());
        dictType.setCreateBy(dictTypeDto.getSimpleUser().getUserName());
        return dictTypeMapper.insert(dictType);
    }


    @Override
    public DictType selectDictTypeById(Long dictId) {
        return dictTypeMapper.selectById(dictId);
    }

    @Override
    public int update(DictTypeDto dictTypeDto) {
        DictType dictType = new DictType();
        BeanUtil.copyProperties(dictTypeDto,dictType);
        dictType.setUpdateBy(dictTypeDto.getSimpleUser().getUserName());
        return dictTypeMapper.updateById(dictType);
    }

    @Override
    public int deleteDictTypeById(Long[] dictIds) {
        List<Long> ids = Arrays.asList(dictIds);
        if (ids!=null&&ids.size()>0){
            return dictTypeMapper.deleteBatchIds(ids);
        }
        return -1;
    }

    @Override
    public DataGridView list() {
        //只查状态正常的数据
        QueryWrapper<DictType> qw=new QueryWrapper<>();
        qw.eq(DictType.COL_STATUS, Constants.STATUS_TRUE);
        return new DataGridView(null,dictTypeMapper.selectList(qw));
    }

    //这个地方有问题
    @Override
    public Boolean checkDictTypeUnique(Long dictId, String dictType) {
        dictId=(dictId==null)?-1L:dictId;
        QueryWrapper<DictType> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq(DictType.COL_DICT_TYPE,dictType);
        DictType type = dictTypeMapper.selectOne(queryWrapper);
        if (null!=type && dictId.longValue()!=type.getDictId().longValue()){
            return true;
        }
        return false;
    }

    @Override
    public void dictCacheAsync() {
        QueryWrapper<DictType> qw=new QueryWrapper<>();
        qw.ge(DictType.COL_STATUS,Constants.STATUS_TRUE);
        List<DictType> dictTypes = dictTypeMapper.selectList(qw);
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        for (DictType dictType : dictTypes) {
            QueryWrapper<DictData> qdw=new QueryWrapper<>();
            qdw.ge(DictData.COL_STATUS,Constants.STATUS_TRUE);
            qdw.eq(DictData.COL_DICT_TYPE,dictType.getDictType());
            List<DictData> dictData = dictDataMapper.selectList(qdw);
            ops.set(Constants.DICT_REDIS_PROFIX+dictType.getDictType(), JSON.toJSONString(dictData));
        }
    }
}
