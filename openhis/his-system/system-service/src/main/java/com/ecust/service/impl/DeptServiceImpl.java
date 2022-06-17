package com.ecust.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ecust.constants.Constants;
import com.ecust.dto.DeptDto;
import com.ecust.vo.DataGridView;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecust.mapper.DeptMapper;
import com.ecust.domain.Dept;
import com.ecust.service.DeptService;
@Service
public class DeptServiceImpl implements DeptService{

    @Autowired
    DeptMapper deptMapper;
    @Override
    public DataGridView listForPage(DeptDto deptDto) {
        QueryWrapper<Dept> queryWrapper=new QueryWrapper<>();
        Page<Dept> page=new Page<>(deptDto.getPageNum(),deptDto.getPageSize());
        queryWrapper.like(StringUtils.isNoneBlank(deptDto.getDeptName()),Dept.COL_DEPT_NAME,deptDto.getDeptName());
        queryWrapper.eq(StringUtils.isNoneBlank(deptDto.getStatus()),Dept.COL_STATUS,deptDto.getStatus());
        queryWrapper.ge(deptDto.getBeginTime()!=null,Dept.COL_CREATE_TIME,deptDto.getBeginTime());
        queryWrapper.le(deptDto.getEndTime()!=null,Dept.COL_CREATE_TIME,deptDto.getEndTime());
        deptMapper.selectPage(page,queryWrapper);
        return new DataGridView(page.getTotal(),page.getRecords());
    }

    @Override
    public List<Dept> list() {
        QueryWrapper<Dept> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq(Dept.COL_STATUS, Constants.STATUS_TRUE);
        return deptMapper.selectList(queryWrapper);
    }

    @Override
    public int addDept(DeptDto deptDto) {
        Dept dept = new Dept();
        BeanUtil.copyProperties(deptDto,dept);
        dept.setCreateTime(DateUtil.date());
        dept.setCreateBy(deptDto.getSimpleUser().getUserName());
        return deptMapper.insert(dept);
    }

    @Override
    public int deleteDeptByIds(Long[] deptIds) {
        if(deptIds==null||deptIds.length==0){
            return 0;
        }
        return deptMapper.deleteBatchIds(Arrays.asList(deptIds));
    }

    @Override
    public int updateDept(DeptDto deptDto) {
        Dept dept = new Dept();
        BeanUtil.copyProperties(deptDto,dept);
        dept.setUpdateBy(deptDto.getSimpleUser().getUserName());
        //修改时间数据库自动完成
        return deptMapper.updateById(dept);
    }

    @Override
    public Dept getOne(Long deptId) {
        return deptMapper.selectById(deptId);
    }

    @Override
    public List<Dept> listDeptByDeptIds(List<Long> deptIds) {
        return deptMapper.selectBatchIds(deptIds);

    }

    @Override
    public void updateDeptRegNumber(Long deptId, Integer regNumber) {
        Dept dept = new Dept();
        dept.setDeptId(deptId);
        dept.setRegNumber(regNumber);
        deptMapper.updateById(dept);
    }
}
