package com.ecust.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ecust.constants.Constants;
import com.ecust.domain.User;
import com.ecust.dto.RoleDto;
import com.ecust.vo.DataGridView;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecust.domain.Role;
import com.ecust.mapper.RoleMapper;
import com.ecust.service.RoleService;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    RoleMapper roleMapper;

    @Override
    public DataGridView listRoleForPage(RoleDto roleDto) {
        Page<Role> page = new Page<>(roleDto.getPageNum(), roleDto.getPageSize());
        QueryWrapper<Role> qw = new QueryWrapper<>();
        qw.like(StringUtils.isNoneBlank(roleDto.getRoleName()), Role.COL_ROLE_NAME, roleDto.getRoleName());
        qw.eq(StringUtils.isNoneBlank(roleDto.getStatus()), Role.COL_STATUS, roleDto.getStatus());
        qw.like(StringUtils.isNoneBlank(roleDto.getRoleCode()), Role.COL_ROLE_CODE, roleDto.getRoleCode());
        qw.ge(roleDto.getBeginTime() != null, Role.COL_CREATE_TIME, roleDto.getBeginTime());
        qw.le(roleDto.getEndTime() != null, Role.COL_CREATE_TIME, roleDto.getEndTime());
        qw.orderByAsc(Role.COL_ROLE_SORT);
        roleMapper.selectPage(page, qw);
        return new DataGridView(page.getTotal(), page.getRecords());
    }

    @Override
    public Role getRoleById(Long roleId) {
        if (roleId != null) {
            return roleMapper.selectById(roleId);
        }
        return null;
    }

    @Override
    public int addRole(RoleDto roleDto) {
        Role role = new Role();
        BeanUtil.copyProperties(roleDto, role);
        role.setCreateBy(roleDto.getSimpleUser().getUserName());
        role.setCreateTime(DateUtil.date());
        return roleMapper.insert(role);
    }

    @Override
    public int updateRole(RoleDto roleDto) {
        Role role = new Role();
        BeanUtil.copyProperties(roleDto, role);
        role.setUpdateBy(roleDto.getSimpleUser().getUserName());
        return this.roleMapper.updateById(role);
    }

    @Override
    public int deleteRoleByIds(Long[] roleIds) {
        if (roleIds == null || roleIds.length == 0) {
            //认为没有删除的数据
            return 0;
        }
        List<Long> ids = Arrays.asList(roleIds);
        roleMapper.deleteRoleMenuByIds(ids);
        roleMapper.deleteRoleUserByIds(ids);
        return roleMapper.deleteBatchIds(ids);

    }

    @Override
    public void saveRoleMenu(Long roleId, Long[] menuIds) {
        roleMapper.deleteRoleMenuByIds(Arrays.asList(roleId));
        for (Long menuId : menuIds) {
            roleMapper.saveRoleMenu(roleId, menuId);
        }
    }

    @Override
    public List<Role> queryAllRole() {
        QueryWrapper<Role> qw = new QueryWrapper<>();
        qw.eq(Role.COL_STATUS, Constants.STATUS_TRUE);
        qw.orderByAsc(Role.COL_ROLE_SORT);
        return this.roleMapper.selectList(qw);
    }

    @Override
    public List<Long> getRoleIdsByUserId(Long userId) {
        if (null == userId) {
            return Collections.EMPTY_LIST;
        }
        return this.roleMapper.selectRoleIdsByUserId(userId);
    }
}
