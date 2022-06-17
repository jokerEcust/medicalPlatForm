package com.ecust.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ecust.constants.Constants;
import com.ecust.domain.SimpleUser;
import com.ecust.dto.MenuDto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecust.mapper.MenuMapper;
import com.ecust.domain.Menu;
import com.ecust.service.MenuService;
@Service
public class MenuServiceImpl implements MenuService{
    @Autowired
    MenuMapper menuMapper;
    @Override
    public List<Menu> selectMenuTree(boolean isAdmin, SimpleUser simpleUser) {
        QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(Menu.COL_STATUS, Constants.STATUS_TRUE);
        queryWrapper.in(Menu.COL_MENU_TYPE,Constants.MENU_TYPE_C,Constants.MENU_TYPE_M);
        queryWrapper.orderByAsc(Menu.COL_PARENT_ID);
        if (isAdmin){//说明是超级管理员
            return menuMapper.selectList(queryWrapper);
        }
        else {
            Serializable userId = simpleUser.getUserId();
            //查询role_user表中的对应role_id，再去role_menu表查询所有的menu_id,再去menu表中查询menu的详细数据
            return menuMapper.selectList(queryWrapper);
        }

    }

    @Override
    public List<Menu> listAllMenus(MenuDto menuDto) {
        QueryWrapper<Menu> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq(StringUtils.isNoneBlank(menuDto.getStatus()),Menu.COL_STATUS,menuDto.getStatus());
        List<Menu> menus = menuMapper.selectList(queryWrapper);
        return  menus;
    }

    @Override
    public Menu getOne(Long menuId) {
        if (menuId!=null){
            return menuMapper.selectById(menuId);
        }
        return null;
    }

    @Override
    public int addMenu(MenuDto menuDto) {
        Menu menu=new Menu();
        BeanUtil.copyProperties(menuDto,menu);
        menu.setCreateBy(menuDto.getSimpleUser().getUserName());
        return menuMapper.insert(menu);
    }

    @Override
    public int deleteMenuById(Long menuId) {
        if (menuId!=null){
            return menuMapper.deleteById(menuId);
        }
        return 0;
    }

    @Override
    public int updateMenu(MenuDto menuDto) {
        Menu menu=new Menu();
        BeanUtil.copyProperties(menuDto,menu);
        menu.setUpdateBy(menuDto.getSimpleUser().getUserName());
        return menuMapper.updateById(menu);
    }

    @Override
    public boolean hasChildByMenuId(Long menuId) {
        return menuMapper.queryChildCountByMenuId(menuId)>0L?true:false;
    }

    @Override
    public List<Long> getMenuIdsByRoleId(Long roleId) {
        return menuMapper.queryMenuIdsByRoleId(roleId);
    }
}
