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
import java.math.BigDecimal;
import java.util.Date;

/**
    * 药用处方表
    */
@ApiModel(value="com-ecust-dto-CareOrderDto")
@Data
@EqualsAndHashCode(callSuper=true)
@AllArgsConstructor
@NoArgsConstructor
public class CareOrderDto extends BaseDto {
    /**
     * 处方ID
     */
    @ApiModelProperty(value="处方ID")
    private String coId;

    /**
     * 处方类型0药用处方1检查处方
     */
    @NotBlank(message = "处方类型不能为空")
    @ApiModelProperty(value="处方类型0药用处方1检查处方")
    private String coType;

    /**
     * 医生id
     */
    @ApiModelProperty(value="医生id")
    private Long userId;

    /**
     * 患者id
     */
    @ApiModelProperty(value="患者id")
    private String patientId;

    /**
     * 患者姓名
     */
    @ApiModelProperty(value="患者姓名")
    private String patientName;

    /**
     * 关联病历id
     */
    @NotBlank(message = "病历id不能为空")
    @ApiModelProperty(value="关联病历id")
    private String chId;

    /**
     * 处方全额
     */
    @NotBlank(message = "处方全额不能为空")
    @ApiModelProperty(value="处方全额")
    private BigDecimal allAmount;
}