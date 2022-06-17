package com.ecust.service;

import com.ecust.domain.LoginInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ecust.dto.LoginInfoDto;
import com.ecust.vo.DataGridView;

public interface LoginInfoService{
    /**
     * 添加
     * @param loginInfo 登录对象
     * @return
     */
    int insertLoginInfo(LoginInfo loginInfo);

    /**
     * 分页查询
     * @param loginInfoDto
     * @return
     */
    DataGridView listForPage(LoginInfoDto loginInfoDto);

    /**
     * 删除登录信息
     * @param infoIds
     * @return
     */
    int deleteLoginInfoByIds(Long[] infoIds);

    /**
     * 清空登录记录
     * @return
     */
    int clearLoginInfo();
}
