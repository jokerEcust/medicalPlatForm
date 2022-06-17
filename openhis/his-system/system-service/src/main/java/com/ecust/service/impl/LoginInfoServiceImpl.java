package com.ecust.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ecust.dto.LoginInfoDto;
import com.ecust.vo.DataGridView;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecust.domain.LoginInfo;
import com.ecust.mapper.LoginInfoMapper;
import com.ecust.service.LoginInfoService;
@Service
public class LoginInfoServiceImpl implements LoginInfoService{
    @Autowired
    LoginInfoMapper loginInfoMapper;
    @Override
    public int insertLoginInfo(LoginInfo loginInfo) {
        //添加的信息包括：名称、账户、IP地址、网络位置、浏览器型号、操作系统、登陆状态、登录类型、msg、登陆时间
        return loginInfoMapper.insert(loginInfo);
    }

    @Override
    public DataGridView listForPage(LoginInfoDto loginInfoDto) {
        //前端传过来的数据有：username,ipaddr,loginaccount,loginstatus,begintime,endtime,pageNum,pageSize
        QueryWrapper<LoginInfo> qw=new QueryWrapper<>();
        Page<LoginInfo> page=new Page<>(loginInfoDto.getPageNum(),loginInfoDto.getPageSize());
        qw.like(StringUtils.isNoneBlank(loginInfoDto.getLoginAccount()),LoginInfo.COL_LOGIN_ACCOUNT,loginInfoDto.getLoginAccount());
        qw.like(StringUtils.isNoneBlank(loginInfoDto.getIpAddr()),LoginInfo.COL_IP_ADDR,loginInfoDto.getIpAddr());
        qw.like(StringUtils.isNoneBlank(loginInfoDto.getUserName()),LoginInfo.COL_USER_NAME,loginInfoDto.getUserName());
        qw.eq(StringUtils.isNoneBlank(loginInfoDto.getLoginStatus()),LoginInfo.COL_LOGIN_STATUS,loginInfoDto.getLoginStatus());
        qw.eq(StringUtils.isNoneBlank(loginInfoDto.getLoginType()),LoginInfo.COL_LOGIN_TYPE,loginInfoDto.getLoginType());
        qw.le(loginInfoDto.getEndTime()!=null,LoginInfo.COL_LOGIN_TIME,loginInfoDto.getEndTime());
        qw.ge(loginInfoDto.getBeginTime()!=null,LoginInfo.COL_LOGIN_TIME,loginInfoDto.getBeginTime());
        loginInfoMapper.selectPage(page,qw);
        return new DataGridView(page.getTotal(),page.getRecords());
    }

    @Override
    public int deleteLoginInfoByIds(Long[] infoIds) {
        if (infoIds==null||infoIds.length==0){
            return 0;
        }
        return loginInfoMapper.deleteBatchIds(Arrays.asList(infoIds));
    }

    @Override
    public int clearLoginInfo() {
        return loginInfoMapper.delete(null);
    }
}
