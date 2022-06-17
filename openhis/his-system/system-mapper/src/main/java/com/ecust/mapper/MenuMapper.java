package com.ecust.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ecust.domain.Menu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MenuMapper extends BaseMapper<Menu> {
    /**
     * 根据菜单id查询子节点个数
     * @param menuId
     * @return
     */
    Long queryChildCountByMenuId(Long menuId);

    List<Long> queryMenuIdsByRoleId(@Param("roleId") Long roleId);
}