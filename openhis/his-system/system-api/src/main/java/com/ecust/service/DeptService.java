package com.ecust.service;

import com.ecust.domain.Dept;
import com.ecust.dto.DeptDto;
import com.ecust.vo.DataGridView;

import java.util.List;

public interface DeptService{
    DataGridView listForPage(DeptDto deptDto);

    List<Dept> list();

    int addDept(DeptDto deptDto);

    int deleteDeptByIds(Long[] deptIds);

    int updateDept(DeptDto deptDto);

    Dept getOne(Long deptId);

    List<Dept> listDeptByDeptIds(List<Long> deptIds);

    void updateDeptRegNumber(Long deptId, Integer regNumber);

}
