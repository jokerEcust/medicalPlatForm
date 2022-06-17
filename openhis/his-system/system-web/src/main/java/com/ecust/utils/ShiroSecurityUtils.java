package com.ecust.utils;

import com.ecust.constants.Constants;
import com.ecust.domain.SimpleUser;
import com.ecust.domain.User;
import com.ecust.vo.ActiveUser;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import java.util.List;

public class ShiroSecurityUtils {

    /***
     * @Description: 得到当前登陆的用户对象的ActiveUser(即当前用户的角色与权限)
     */
    public static ActiveUser getCurrentActiveUser(){
        Subject subject= SecurityUtils.getSubject();
        ActiveUser activerUser= (ActiveUser) subject.getPrincipal();
        return activerUser;
    }

    /***
     * @Description: 得到当前登陆的用户对象User
     */
    public static User getCurrentUser(){
        return getCurrentActiveUser().getUser();
    }

    /***
     * @Description: 得到当前登陆的用户对象SimpleUser（id与名字）
     */
    public static SimpleUser getCurrentSimpleUser(){
        User user = getCurrentActiveUser().getUser();
        return new SimpleUser(user.getUserId(),user.getUserName());
    }

    /***
     * @Description: 得到当前登陆的用户名称
     */
    public static String getCurrentUserName(){
        return getCurrentActiveUser().getUser().getUserName();
    }

    /***
     * @Description: 得到当前登陆对象的角色编码
     */
    public static List<String> getCurrentUserRoles(){
        return getCurrentActiveUser().getRoles();
    }


    /***
     * @Description: 得到当前登陆对象的权限编码
     */
    public static List<String> getCurrentUserPermissions(){
        return getCurrentActiveUser().getPermissions();
    }

    /***
     * @Description: 判断当前用户是否是超管
     */
    public static boolean isAdmin(){
        return getCurrentUser().getUserType().equals(Constants.USER_ADMIN);
    }
}
