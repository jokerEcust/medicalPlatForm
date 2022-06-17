package com.ecust.service;

import com.ecust.domain.DictType;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ecust.dto.DictTypeDto;
import com.ecust.vo.DataGridView;

public interface DictTypeService {
    /**
     * 分页查询字典类型
     *
     * @param dictTypeDto
     * @return
     */
    DataGridView listPage(DictTypeDto dictTypeDto);

    /**
     * 添加字典类型
     *
     * @param dictTypeDto
     * @return
     */
    int insert(DictTypeDto dictTypeDto);

    /**
     * 根据id查询字典信息
     *
     * @param dictId
     * @return
     */
    DictType selectDictTypeById(Long dictId);

    /**
     * 更新字典信息
     *
     * @param dictTypeDto
     * @return
     */
    int update(DictTypeDto dictTypeDto);

    /**
     * 根据id删除字典类型
     *
     * @param dictIds
     * @return
     */
    int deleteDictTypeById(Long[] dictIds);

    /**
     * 查询所有字典类型
     *
     * @return
     */
    DataGridView list();

    /**
     * 检查字典类型是否存在
     *
     * @param dictType
     * @return
     */
    Boolean checkDictTypeUnique(Long dictId, String dictType);

    /**
     * 同步缓存
     */
    void dictCacheAsync();
}
