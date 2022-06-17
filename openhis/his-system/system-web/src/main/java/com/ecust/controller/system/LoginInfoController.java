package com.ecust.controller.system;

import com.ecust.dto.LoginInfoDto;
import com.ecust.service.LoginInfoService;
import com.ecust.vo.AjaxResult;
import com.ecust.vo.DataGridView;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Log4j2
@RequestMapping("/system/loginInfo")
public class LoginInfoController {
    @Autowired
    LoginInfoService loginInfoService;

    @GetMapping("/listForPage")
    //感觉这里的接口文档有问题，传入的时候没有LoginType，但是应该存在
    public AjaxResult listForPage(LoginInfoDto loginInfoDto){
        DataGridView dataGridView = loginInfoService.listForPage(loginInfoDto);
        return AjaxResult.success("查询成功",dataGridView.getData());
    }
    @DeleteMapping("/deleteLoginInfoByIds/{infoIds}")
    public AjaxResult deleteOperLogByIds(@PathVariable Long[] infoIds) {
        return AjaxResult.toAjax(loginInfoService.deleteLoginInfoByIds(infoIds));
    }

    @DeleteMapping("/clearLoginInfo")
    public AjaxResult clearAllOperLog() {
        return AjaxResult.toAjax(loginInfoService.clearLoginInfo());
    }



}
