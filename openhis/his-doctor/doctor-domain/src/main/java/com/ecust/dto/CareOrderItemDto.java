package com.ecust.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ecust.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 开诊细表
 */
@ApiModel(value = "com-ecust-dto-CareOrderItemDto")
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class CareOrderItemDto extends BaseDto {
    /**
     * 开诊明细ID
     */
    @ApiModelProperty(value = "开诊明细ID")
    private String itemId;

    /**
     * 所属处方id
     */
    @ApiModelProperty(value = "所属处方id")
    private String coId;

    /**
     * 药品或者检查项目id
     */
    @NotBlank(message = "药品或检查项目 ID 不能为空")
    @ApiModelProperty(value = "药品或者检查项目id")
    private String itemRefId;

    /**
     * 药品或检查项目名称
     */
    @NotBlank(message = "药品或检查项目名称不能为空")
    @ApiModelProperty(value = "药品或检查项目名称")
    private String itemName;

    /**
     * 项目类型0药品  还是1检查项
     */
    @NotBlank(message = "项目类型不能为空")
    @ApiModelProperty(value = "项目类型0药品  还是1检查项")
    private String itemType;

    /**
     * 数量
     */
    @ApiModelProperty(value = "数量")
    private BigDecimal num;

    /**
     * 单价
     */
    @NotNull(message = "单价不能为空")
    @ApiModelProperty(value = "单价")
    private BigDecimal price;

    /**
     * 金额
     */
    @NotNull(message = "金额不能为空")
    @ApiModelProperty(value="金额")
    private BigDecimal amount;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;
}