package com.ecust.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Date;

@ApiModel(value="com-ecust-domain-DictType")
@Data
@EqualsAndHashCode(callSuper=true)
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "sys_dict_type")
public class DictTypeDto extends BaseDto {
    /**
     * 字典主键
     */
    @ApiModelProperty(value="字典主键")
    private Long dictId;

    /**
     * 字典名称
     */
    @NotNull(message = "字典名称不能为空")
    @ApiModelProperty(value="字典名称")
    private String dictName;

    /**
     * 字典类型
     */
    @NotNull(message = "字典类型不能为空")
    @ApiModelProperty(value="字典类型")
    private String dictType;

    /**
     * 状态（0正常 1停用）
     */
    @ApiModelProperty(value="状态（0正常 1停用）")
    @NotNull(message = "状态不能为空")
    private String status;

    /**
     * 创建者
     */
    @ApiModelProperty(value="创建者")
    private String createBy;

    /**
     * 创建时间
     */
    @ApiModelProperty(value="创建时间")
    private Date createTime;

    /**
     * 更新者
     */
    @ApiModelProperty(value="更新者")
    private String updateBy;

    /**
     * 更新时间
     */
    @ApiModelProperty(value="更新时间")
    private Date updateTime;

    /**
     * 备注
     */
    @ApiModelProperty(value="备注")
    private String remark;
}
