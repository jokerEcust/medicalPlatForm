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

@ApiModel(value="com-ecust-dto-InventoryLogDto")
@Data
@EqualsAndHashCode(callSuper=true)
@AllArgsConstructor
@NoArgsConstructor
public class InventoryLogDto extends BaseDto {
    /**
     * 入库ID
     */
    @ApiModelProperty(value="入库ID")
    private String inventoryLogId;

    /**
     * 采购单据ID
     */
    @ApiModelProperty(value="采购单据ID")
    private String purchaseId;

    /**
     * 药品ID
     */
    @ApiModelProperty(value="药品ID")
    private String medicinesId;

    /**
     * 入库数量
     */
    @ApiModelProperty(value="入库数量")
    private Integer inventoryLogNum;

    /**
     * 批发价
     */
    @ApiModelProperty(value="批发价")
    private BigDecimal tradePrice;

    /**
     * 批发额
     */
    @ApiModelProperty(value="批发额")
    private BigDecimal tradeTotalAmount;

    /**
     * 药品生产批次号
     */
    @ApiModelProperty(value="药品生产批次号")
    private String batchNumber;

    /**
     * 药品名称
     */
    @ApiModelProperty(value="药品名称")
    private String medicinesName;

    /**
     * 药品分类 sys_dict_data表his_medicines_type
     */
    @ApiModelProperty(value="药品分类 sys_dict_data表his_medicines_type")
    private String medicinesType;

    /**
     * 处方类型 sys_dict_data表his_prescription_type
     */
    @ApiModelProperty(value="处方类型 sys_dict_data表his_prescription_type")
    private String prescriptionType;

    /**
     * 生产厂家ID
     */
    @ApiModelProperty(value="生产厂家ID")
    private String producterId;

    /**
     * 换算量
     */
    @ApiModelProperty(value="换算量")
    private Integer conversion;

    /**
     * 单位（g/条）
     */
    @ApiModelProperty(value="单位（g/条）")
    private String unit;
}