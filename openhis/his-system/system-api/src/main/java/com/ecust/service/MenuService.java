package com.ecust.service;

import com.ecust.domain.Menu;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ecust.domain.SimpleUser;
import com.ecust.dto.MenuDto;

import java.util.List;

public interface MenuService{
    /**
     * 查询菜单信息
     * @param isAdmin 用户权限
     * @param simpleUser 用户信息（姓名，id）
     * @return 该用户可以查看的菜单树
     */
    List<Menu> selectMenuTree(boolean isAdmin, SimpleUser simpleUser);

    /**
     * 查询所有的menu
     * @param menuDto
     * @return
     */
    List<Menu> listAllMenus(MenuDto menuDto);

    /**
     * 根据id查询菜单
     * @param menuId
     * @return
     */
    Menu getOne(Long menuId);

    /**
     * 添加menu对象
     * @param menuDto
     * @return
     */
    int addMenu(MenuDto menuDto);

    /**
     * 删除menu根据id
     * @param menuId
     * @return
     */
    int deleteMenuById(Long menuId);

    /**
     * 更新menu
     * @param menuDto
     * @return
     */
    int updateMenu(MenuDto menuDto);

    boolean hasChildByMenuId(Long menuId);

    List<Long> getMenuIdsByRoleId(Long roleId);
}
