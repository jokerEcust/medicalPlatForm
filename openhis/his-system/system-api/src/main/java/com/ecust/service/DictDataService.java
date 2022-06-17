package com.ecust.service;

import com.ecust.domain.DictData;
import com.ecust.domain.DictType;
import com.ecust.dto.DictDataDto;
import com.ecust.vo.DataGridView;

import java.util.List;

public interface DictDataService{
    /**
     * 分页查询字典数据
     *
     * @param dictDataDto
     * @return
     */
    DataGridView listPage(DictDataDto dictDataDto);

    /**
     * 添加字典类型
     *
     * @param dictDataDto
     * @return
     */
    int insert(DictDataDto dictDataDto);

    /**
     * 根据id查询字典数据信息
     *
     * @param dictCode
     * @return
     */
    DictData selectDictDataById(Long dictCode);

    /**
     * 更新字典数据信息
     *
     * @param dictDataDto
     * @return
     */
    int update(DictDataDto dictDataDto);

    /**
     * 根据id删除字典数据
     *
     * @param dictCodeIds
     * @return
     */
    int deleteDictDataByIds(Long[] dictCodeIds);

    /*** 根据字典类型查询字典数据 * @param dictType * @return */
    List<DictData> selectDictDataByDictType(String dictType);


}
