package com.ecust.controller.system;

import com.ecust.aspectj.annotation.Log;
import com.ecust.aspectj.enums.BusinessType;
import com.ecust.domain.Role;
import com.ecust.domain.User;
import com.ecust.dto.RoleDto;
import com.ecust.service.RoleService;
import com.ecust.utils.ShiroSecurityUtils;
import com.ecust.vo.AjaxResult;
import com.ecust.vo.DataGridView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/system/role")
public class RoleController {
    @Autowired
    RoleService roleService;

    @GetMapping("/listRoleForPage")
    public AjaxResult listRoleForPage(RoleDto roleDto) {
        DataGridView dataGridView =roleService.listRoleForPage(roleDto);
        return AjaxResult.success("查询成功", dataGridView.getData(),dataGridView.getTotal());
    }

    @GetMapping("getRoleById/{roleId}")
    public AjaxResult getRoleById(@PathVariable Long roleId) {
        Role role = roleService.getRoleById(roleId);
        return AjaxResult.success("查询成功", role);
    }

    @PostMapping("/addRole")
    @Log(title = "添加角色",businessType = BusinessType.INSERT)
    public AjaxResult addRole(@Valid RoleDto roleDto) {
        roleDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
        return AjaxResult.toAjax(roleService.addRole(roleDto));
    }

    @PutMapping("/updateRole")
    @Log(title = "更新角色",businessType = BusinessType.UPDATE)
    public AjaxResult updateRole(@Valid RoleDto roleDto) {
        roleDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
        return AjaxResult.toAjax(roleService.updateRole(roleDto));

    }
    @DeleteMapping("/deleteRoleByIds/{roleIds}")
    @Log(title = "删除角色",businessType = BusinessType.DELETE)
    public AjaxResult deleteRoleByIds(@PathVariable Long[] roleIds){
        return AjaxResult.toAjax(roleService.deleteRoleByIds(roleIds));
    }

    @PostMapping("/saveRoleMenu/{roleId}/{menuIds}")
    @Log(title = "更新用户菜单权限",businessType = BusinessType.UPDATE)
    public AjaxResult saveRoleMenu(@PathVariable Long roleId,@PathVariable Long[] menuIds){
        if(menuIds.length==1&&menuIds[0].equals(-1L)){
            menuIds=new Long[]{};//设定没有角色的菜单权限为空
        }
        roleService.saveRoleMenu(roleId,menuIds);
        return AjaxResult.success();
    }

    @GetMapping("/selectAllRole")
    public AjaxResult selectAllRole(){
        List<Role> list=roleService.queryAllRole();
        return AjaxResult.success(list);
    }

    @GetMapping("/getRoleIdsByUserId/{userId}")
    public AjaxResult getRoleIdsByUserId(@PathVariable Long userId){
        List<Long> roleIds=roleService.getRoleIdsByUserId(userId);
        return AjaxResult.success(roleIds);
    }


}
