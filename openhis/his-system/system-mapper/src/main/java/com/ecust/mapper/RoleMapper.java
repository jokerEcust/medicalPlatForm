package com.ecust.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ecust.domain.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RoleMapper extends BaseMapper<Role> {
    void deleteRoleMenuByIds(@Param("ids") List<Long> ids);

    void deleteRoleUserByIds(@Param("ids") List<Long> ids);

    void saveRoleMenu(@Param("roleId") Long roleId,@Param("menuId") Long menuId);

    void deleteRoleUserByUserIds(@Param("ids") List<Long> ids);

    List<Long> selectRoleIdsByUserId(Long userId);

    void saveRoleUser(@Param("userId") Long userId, @Param("roleId") Long roleId);
}