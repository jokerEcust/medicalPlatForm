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

import java.math.BigDecimal;
import java.util.Date;

@ApiModel(value="com-ecust-domain-Purchase")
@Data
@EqualsAndHashCode(callSuper=true)
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseDto extends BaseDto {
    /**
     * 制单号ID 全局ID雪花算法
     */
    @ApiModelProperty(value="制单号ID 全局ID雪花算法")
    private String purchaseId;

    /**
     * 供应商ID
     */
    @ApiModelProperty(value="供应商ID")
    private String providerId;

    /**
     * 采购批发总额
     */
    @ApiModelProperty(value="采购批发总额")
    private BigDecimal purchaseTradeTotalAmount;

    /**
     * 单据状态； 1未提交2待审核 3审核通过 4审核失败 5作废 6入库成功 字典表 his_order_status
     */
    @ApiModelProperty(value="单据状态； 1未提交2待审核 3审核通过 4审核失败 5作废 6入库成功 字典表 his_order_status")
    private String status;

    /**
     * 申请人ID
     */
    @ApiModelProperty(value="申请人ID")
    private Long applyUserId;

    /**
     * 申请人名称
     */
    @ApiModelProperty(value="申请人名称")
    private String applyUserName;

    /**
     * 入库操作人
     */
    @ApiModelProperty(value="入库操作人")
    private String storageOptUser;

    /**
     * 入库操作时间
     */
    @ApiModelProperty(value="入库操作时间")
    private Date storageOptTime;

    /**
     * 审核信息
     */
    @ApiModelProperty(value="审核信息")
    private String auditMsg;
}