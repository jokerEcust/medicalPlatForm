package com.ecust.service;

import com.ecust.domain.Role;
import com.ecust.dto.RoleDto;
import com.ecust.vo.DataGridView;

import java.util.List;


public interface RoleService{

    /**
     * 查询所有角色
     * @return
     */
    DataGridView listRoleForPage(RoleDto roleDto);

    /**
     * 通过id获取角色
     * @param roleId
     * @return
     */
    Role getRoleById(Long roleId);

    /**
     * 增加角色对象
     * @param roleDto
     * @return
     */
    int addRole(RoleDto roleDto);

    /**
     * 更新角色对象
     * @param roleDto
     * @return
     */
    int updateRole(RoleDto roleDto);

    int deleteRoleByIds(Long[] roleIds);

    void saveRoleMenu(Long roleId, Long[] menuIds);

    List<Role> queryAllRole();

    List<Long> getRoleIdsByUserId(Long userId);
}
