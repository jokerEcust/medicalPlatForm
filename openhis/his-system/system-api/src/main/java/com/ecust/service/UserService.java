package com.ecust.service;

import com.ecust.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ecust.dto.UserDto;
import com.ecust.vo.DataGridView;

import java.util.List;

public interface UserService{
    /**
     * 根据手机号查询用户信息
     * @param phone 手机号码
     * @return 查询到的对象
     */
    User queryUserByPhone(String phone);

    /**
     * 根据用户id查询用户
     * @param userId 用户id
     * @return 查询到的对象
     */
    User getOne(Long userId);

    DataGridView listUserForPage(UserDto userDto);

    int addUser(UserDto userDto);

    int updateUser(UserDto userDto);

    int deleteUserByIds(Long[] userIds);

    void resetPassWord(Long[] userIds);

    void saveUserRole(Long userId, Long[] roleIds);

    List<User> queryUsersNeedScheduling(Long userId,Long deptId);

    List<User> queryAllUser();

}
