package com.ecust.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ecust.constants.Constants;
import com.ecust.dto.UserDto;
import com.ecust.mapper.RoleMapper;
import com.ecust.utils.AppMd5Utils;
import com.ecust.vo.DataGridView;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

import com.ecust.mapper.UserMapper;
import com.ecust.domain.User;
import com.ecust.service.UserService;
@Service
public class UserServiceImpl implements UserService{

    @Autowired
    RoleMapper roleMapper;

    @Autowired
    UserMapper userMapper;
    @Override
    public User queryUserByPhone(String phone) {
        QueryWrapper<User> qw=new QueryWrapper<>();
        qw.eq(User.COL_PHONE,phone);
        User user = this.userMapper.selectOne(qw);
        return user;
    }

    @Override
    public User getOne(Long userId) {
        return userMapper.selectById(userId);
    }

    @Override
    public DataGridView listUserForPage(UserDto userDto) {
        Page<User> page=new Page<>(userDto.getPageNum(),userDto.getPageSize());
        QueryWrapper<User> qw=new QueryWrapper<>();
        qw.eq(userDto.getDeptId()!=null,User.COL_DEPT_ID,userDto.getDeptId());
        qw.like(StringUtils.isNoneBlank(userDto.getUserName()),User.COL_USER_NAME,userDto.getUserName());
        qw.like(StringUtils.isNoneBlank(userDto.getPhone()),User.COL_PHONE,userDto.getPhone());
        qw.eq(userDto.getStatus()!=null,User.COL_STATUS,userDto.getStatus());
        qw.le(null!=userDto.getBeginTime(),User.COL_CREATE_TIME,userDto.getBeginTime());
        qw.ge(null!=userDto.getEndTime(),User.COL_CREATE_TIME,userDto.getEndTime());
        userMapper.selectPage(page,qw);
        return new DataGridView(page.getTotal(),page.getRecords());
    }

    @Override
    public int addUser(UserDto userDto) {
        User user = new User();
        BeanUtil.copyProperties(userDto,user);
        user.setCreateBy(userDto.getSimpleUser().getUserName());
        user.setCreateTime(DateUtil.date());
        user.setUserType(Constants.USER_NORMAL);
        user.setSalt(AppMd5Utils.createSalt());
        user.setPassword(AppMd5Utils.md5(user.getPhone().substring(5),user.getSalt(),2));
        return userMapper.insert(user);
    }

    @Override
    public int updateUser(UserDto userDto) {
        User user = new User();
        BeanUtil.copyProperties(userDto,user);
        user.setUpdateBy(userDto.getSimpleUser().getUserName());
        return userMapper.updateById(user);
    }

    @Override
    public int deleteUserByIds(Long[] userIds) {
        if (userIds==null||userIds.length==0){
            //认为没有删除用户的操作
            return 0;
        }
        //删除用户的话，用户对应的角色（医生、护士。。）等也要删除
        List<Long> ids = Arrays.asList(userIds);
        roleMapper.deleteRoleUserByIds(ids);
        return userMapper.deleteBatchIds(ids);
    }

    @Override
    public void resetPassWord(Long[] userIds) {
        for (Long userId :
                userIds) {
            User user = userMapper.selectById(userId);
            String defaultPwd="";
            if(user.getUserType().equals(Constants.USER_ADMIN)){
                defaultPwd="123456";
            }
            else {
                defaultPwd=user.getPhone().substring(5);
            }
            user.setSalt(AppMd5Utils.createSalt());
            user.setPassword(AppMd5Utils.md5(defaultPwd,user.getSalt(),2));
            userMapper.updateById(user);
        }
    }

    @Override
    public void saveUserRole(Long userId, Long[] roleIds) {
        //保存之前需要先删除和用户id相同的数值再插入
        roleMapper.deleteRoleUserByUserIds(Arrays.asList(userId));
        for (Long roleId :
                roleIds) {
            roleMapper.saveRoleUser(userId, roleId);
        }
    }

    @Override
    public List<User> queryUsersNeedScheduling(Long userId,Long deptId) {
        QueryWrapper<User> qw=new QueryWrapper<>();
        qw.eq(User.COL_STATUS,Constants.STATUS_TRUE);
        qw.eq(User.COL_SCHEDULING_FLAG,Constants.STATUS_TRUE);
        qw.eq(userId!=null,User.COL_USER_ID,userId);
        qw.eq(deptId!=null,User.COL_DEPT_ID,deptId);
        return userMapper.selectList(qw);
    }

    @Override
    public List<User> queryAllUser() {
        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq(User.COL_STATUS,Constants.STATUS_TRUE);
        return userMapper.selectList(queryWrapper);
    }
}
