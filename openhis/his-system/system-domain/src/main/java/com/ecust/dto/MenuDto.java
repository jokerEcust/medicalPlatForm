package com.ecust.dto;

import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@ApiModel(value="com-ecust-domain-MenuDto")
@Data
@EqualsAndHashCode(callSuper=true)
@AllArgsConstructor
@NoArgsConstructor
public class MenuDto extends BaseDto{
    /**
     * 菜单ID
     */
    @ApiModelProperty(value="菜单ID")
    private Long menuId;

    /**
     * 父菜单ID
     */
    @ApiModelProperty(value="父菜单ID")
    private Long parentId;

    /**
     * 父节点ID集合
     */
    @ApiModelProperty(value="父节点ID集合")
    private String parentIds;

    /**
     * 菜单名称
     */
    @NotNull(message = "菜单名称不能为空")
    @ApiModelProperty(value="菜单名称")
    private String menuName;

    /**
     * 菜单类型（M目录 C菜单 F按钮）
     */
    @NotNull(message = "菜单类型不能为空")
    @ApiModelProperty(value="菜单类型（M目录 C菜单 F按钮）")
    @Pattern(regexp = "['M','C','F']")
    private String menuType;

    /**
     * 权限标识
     */
    @ApiModelProperty(value="权限标识")
    private String percode;

    /**
     * 路由地址
     */
    @ApiModelProperty(value="路由地址")
    private String path;

    /**
     * 备注
     */
    @ApiModelProperty(value="备注")
    private String remark;

    /**
     * 菜单状态（0正常 1停用）
     */
    @NotNull(message = "菜单状态不能为空")
    @Pattern(regexp = "[0,1]")
    @ApiModelProperty(value="菜单状态（0正常 1停用）")
    private String status;

}
