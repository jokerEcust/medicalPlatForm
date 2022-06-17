package com.ecust.controller.system;


import com.ecust.domain.Role;
import com.ecust.domain.User;
import com.ecust.dto.UserDto;
import com.ecust.service.UserService;
import com.ecust.utils.ShiroSecurityUtils;
import com.ecust.vo.AjaxResult;
import com.ecust.vo.DataGridView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/system/user")
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping("/listUserForPage")
    public AjaxResult listUserForPage(UserDto userDto) {
        DataGridView dataGridView = userService.listUserForPage(userDto);
        return AjaxResult.success("查询成功", dataGridView.getData(), dataGridView.getTotal());
    }

    @PostMapping("/addUser")
    public AjaxResult addUser(@Valid UserDto userDto) {
        userDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
        return AjaxResult.toAjax(userService.addUser(userDto));
    }

    @GetMapping("/getUserById/{userId}")
    public AjaxResult getUserById(@PathVariable Long userId) {
        return AjaxResult.success(userService.getOne(userId));
    }

    @PutMapping("/updateUser")
    public AjaxResult updateUser(@Valid UserDto userDto) {
        userDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
        return AjaxResult.toAjax(userService.updateUser(userDto));
    }

    @DeleteMapping("/deleteUserByIds/{userIds}")
    public AjaxResult deleteUserByIds(@PathVariable Long[] userIds) {
        return AjaxResult.toAjax(userService.deleteUserByIds(userIds));
    }

    @PutMapping("/resetPwd/{userIds}")
    public AjaxResult resetPwd(@PathVariable Long[] userIds) {
        if (userIds.length > 0) {
            userService.resetPassWord(userIds);
            return AjaxResult.success("重装密码成功");
        }
        return AjaxResult.fail("重置失败");
    }

    @PostMapping("/saveUserRole")
    public AjaxResult saveUserRole(Long userId, Long[] roleIds) {
        if(roleIds.length==1&&roleIds[0].equals(-1L)){
            roleIds=new Long[]{};
        }
        userService.saveUserRole(userId,roleIds);
        return AjaxResult.success();
    }

    @GetMapping("/selectAllUser")
    public AjaxResult selectAllUser() {
        List<User> list=userService.queryAllUser();
        return AjaxResult.success(list);
    }


    @GetMapping("/getUsersNeedScheduling")
    public AjaxResult getUsersNeedScheduling(Long deptId) {
        List<User> list=userService.queryUsersNeedScheduling(null,deptId);
        return AjaxResult.success(list);
//        return null;
    }
}
