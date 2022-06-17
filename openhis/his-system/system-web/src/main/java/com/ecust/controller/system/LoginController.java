package com.ecust.controller.system;

import cn.hutool.core.date.DateUtil;
import com.ecust.aspectj.annotation.Log;
import com.ecust.constants.Constants;
import com.ecust.constants.HttpStatus;
import com.ecust.domain.LoginInfo;
import com.ecust.domain.Menu;
import com.ecust.domain.SimpleUser;
import com.ecust.dto.LoginBodyDto;
import com.ecust.service.LoginInfoService;
import com.ecust.service.MenuService;
import com.ecust.utils.AddressUtils;
import com.ecust.utils.IpUtils;
import com.ecust.utils.ShiroSecurityUtils;
import com.ecust.vo.ActiveUser;
import com.ecust.vo.AjaxResult;
import com.ecust.vo.MenuTreeVo;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.extern.log4j.Log4j2;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 系统登陆控制器
 */
@RestController
@Log4j2
public class LoginController {
    @Autowired
    MenuService menuService;

    @Autowired
    LoginInfoService loginInfoService;

    /**
     * 登录
     *
     * @return
     */
    @PostMapping("/login/doLogin")
    public AjaxResult login(@Valid @RequestBody LoginBodyDto loginBodyDto, HttpServletRequest request) {
        System.out.println("登录进入");
        AjaxResult ajaxResult = AjaxResult.success();
        String username = loginBodyDto.getUsername();
        String pwd = loginBodyDto.getPassword();
        //构造用户名与密码的token
        UsernamePasswordToken token = new UsernamePasswordToken(username, pwd);
        Subject subject = SecurityUtils.getSubject();
        //封装用户登录信息
        LoginInfo loginInfo = createLoginInfo(request);
        loginInfo.setLoginAccount(loginBodyDto.getUsername());
        try {
            subject.login(token);
            Serializable webToken = subject.getSession().getId();
            ajaxResult.put(Constants.TOKEN, webToken);
            loginInfo.setUserName(ShiroSecurityUtils.getCurrentUserName());
            loginInfo.setLoginStatus(Constants.LOGIN_SUCCESS);
            loginInfo.setMsg("登陆成功");
        } catch (Exception e) {
//            e.printStackTrace();
            log.error("用户名或密码不正确", e);
            ajaxResult = AjaxResult.error(HttpStatus.ERROR, "用户名或密码不正确");
            loginInfo.setLoginStatus(Constants.LOGIN_ERROR);
            loginInfo.setMsg("登陆失败");
        }
        loginInfoService.insertLoginInfo(loginInfo);
        return ajaxResult;
    }

    private LoginInfo createLoginInfo(HttpServletRequest request) {
        LoginInfo loginInfo = new LoginInfo();
        final UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
        final String ip = IpUtils.getIpAddr(request);
        String address = AddressUtils.getRealAddressByIP(ip);
        loginInfo.setIpAddr(ip);
        loginInfo.setLoginLocation(address);
        // 获取客户端操作系统
        String os = userAgent.getOperatingSystem().getName();
        // 获取客户端浏览器
        String browser = userAgent.getBrowser().getName();
        loginInfo.setOs(os);
        loginInfo.setBrowser(browser);
        loginInfo.setLoginTime(DateUtil.date());
        loginInfo.setLoginType(Constants.LOGIN_TYPE_SYSTEM);//注意，登录类型（后台人员/病患）这里写死了
        return loginInfo;
    }

    /**
     * 获取信息
     *
     * @return
     */
    @GetMapping("/login/getInfo")
    public AjaxResult getInfo() {
        Subject subject = SecurityUtils.getSubject();
        ActiveUser activeUser = (ActiveUser) subject.getPrincipal();
        AjaxResult ajaxResult = AjaxResult.success();
        ajaxResult.put("username", activeUser.getUser().getUserName());
        ajaxResult.put("picture", activeUser.getUser().getPicture());
        ajaxResult.put("roles", activeUser.getRoles());
        ajaxResult.put("permissions", activeUser.getPermissions());
        return ajaxResult;
    }

    /**
     * 退出登录
     *
     * @return
     */
    @PostMapping("login/logout")
    public AjaxResult logout() {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return AjaxResult.success("用户退出成功");
    }

    @GetMapping("login/getMenus")
    public AjaxResult getMenus() {
        Subject subject = SecurityUtils.getSubject();
        ActiveUser activeUser = (ActiveUser) subject.getPrincipal();
        boolean isAdmin = activeUser.getUser().getUserType().equals(Constants.USER_ADMIN);//看看是否为管理员
        SimpleUser simpleUser = null;
        if (!isAdmin) {
            simpleUser = new SimpleUser(activeUser.getUser().getUserId(), activeUser.getUser().getUserName());
        }
        List<Menu> menus = menuService.selectMenuTree(isAdmin, simpleUser);
        List<MenuTreeVo> menuVos = new ArrayList<>();
        for (Menu menu : menus) {
            menuVos.add(new MenuTreeVo(menu.getMenuId().toString(), menu.getPath()));
        }
        return AjaxResult.success(menuVos);
    }
}
