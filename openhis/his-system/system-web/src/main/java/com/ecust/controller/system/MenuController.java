package com.ecust.controller.system;


import com.ecust.aspectj.annotation.Log;
import com.ecust.aspectj.enums.BusinessType;
import com.ecust.constants.Constants;
import com.ecust.domain.Menu;
import com.ecust.dto.MenuDto;
import com.ecust.service.MenuService;
import com.ecust.utils.ShiroSecurityUtils;
import com.ecust.vo.AjaxResult;
import org.aspectj.weaver.loadtime.Aj;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/system/menu")
public class MenuController {
    @Autowired
    MenuService menuService;

    /**
     * 查询所有菜单及权限
     * @param menuDto
     * @return
     */
    @GetMapping("/listAllMenus")
    public AjaxResult listAllMenus(MenuDto menuDto){
        List<Menu> listAllMenus =menuService.listAllMenus(menuDto);
        return AjaxResult.success("查询成功",listAllMenus);
    }

    /**
     * 查询菜单的下拉树
     * @return
     */
    @GetMapping("/selectMenuTree")
    public AjaxResult selectMenuTree(){
        MenuDto menuDto=new MenuDto();
        menuDto.setStatus(Constants.STATUS_TRUE);
        return AjaxResult.success(this.menuService.listAllMenus(menuDto));
    }

    /**
     * 根据id查询菜单
     * @param menuId
     * @return
     */
    @GetMapping("/getMenuById/{menuId}")
    public AjaxResult getMenuById(@PathVariable Long menuId){
        return AjaxResult.success(menuService.getOne(menuId));
    }

    /**
     * 添加菜单
     * @param menuDto
     * @return
     */
    @PostMapping("/addMenu")
    @Log(title = "添加菜单",businessType = BusinessType.INSERT)
    public AjaxResult addMenu(@Valid MenuDto menuDto){
        menuDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
        return AjaxResult.toAjax(menuService.addMenu(menuDto));
    }

    /**
     * 根据id删除菜单
     * @param menuId
     * @return
     */
    @DeleteMapping("/deleteMenuById/{menuId}")
    @Log(title = "删除菜单",businessType = BusinessType.DELETE)
    public AjaxResult deleteMenuById(@PathVariable Long menuId){
        if (menuService.hasChildByMenuId(menuId)){
            return AjaxResult.fail("当前要删除的菜单有子节点，请先删除子节点");
        }
        return AjaxResult.toAjax(menuService.deleteMenuById(menuId));
    }

    /**
     * 更新菜单
     * @param menuDto
     * @return
     */
    @PutMapping("/updateMenu")
    @Log(title = "更新菜单",businessType = BusinessType.UPDATE)
    public AjaxResult updateMenu(@Valid MenuDto menuDto){
        menuDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
        return AjaxResult.toAjax(menuService.updateMenu(menuDto));
    }

    /**
     * 根据角色ID查询已分配菜单ID[只查子节点]
     * @return
     */
    @GetMapping("/getMenuIdsByRoleId/{roleId}")
    public AjaxResult getMenuIdsByRoleId(@PathVariable Long roleId){
        return AjaxResult.success("查询成功",menuService.getMenuIdsByRoleId(roleId));
    }
}
