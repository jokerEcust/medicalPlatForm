package com.ecust.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@ApiModel(value="com-ecust-domain-CheckResult")
@Data
@EqualsAndHashCode(callSuper=true)
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "his_check_result")
public class CheckResult extends BaseEntity {
    /**
     * 处方检查项ID
     */
    @TableId(value = "item_id", type = IdType.INPUT)
    @ApiModelProperty(value="处方检查项ID")
    private String itemId;

    /**
     * 检查项目ID
     */
    @TableField(value = "check_item_id")
    @ApiModelProperty(value="检查项目ID")
    private Integer checkItemId;

    /**
     * 检查项目名称
     */
    @TableField(value = "check_item_name")
    @ApiModelProperty(value="检查项目名称")
    private String checkItemName;

    /**
     * 价格
     */
    @TableField(value = "price")
    @ApiModelProperty(value="价格")
    private BigDecimal price;

    /**
     * 检查结果描述
     */
    @TableField(value = "result_msg")
    @ApiModelProperty(value="检查结果描述")
    private String resultMsg;

    /**
     * 检查结果扫描附件[json表示]
     */
    @TableField(value = "result_img")
    @ApiModelProperty(value="检查结果扫描附件[json表示]")
    private String resultImg;

    /**
     * 患者ID
     */
    @TableField(value = "patient_id")
    @ApiModelProperty(value="患者ID")
    private String patientId;

    /**
     * 患者姓名
     */
    @TableField(value = "patient_name")
    @ApiModelProperty(value="患者姓名")
    private String patientName;

    /**
     * 是否录入检查结果0 检测中  1 检测完成  字典表 his_check_result_status
     */
    @TableField(value = "result_status")
    @ApiModelProperty(value="是否录入检查结果0 检测中  1 检测完成  字典表 his_check_result_status")
    private String resultStatus;

    /**
     * 关联挂号单号
     */
    @TableField(value = "reg_id")
    @ApiModelProperty(value="关联挂号单号")
    private String regId;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    @ApiModelProperty(value="创建时间")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    @ApiModelProperty(value="更新时间")
    private Date updateTime;

    /**
     * 创建者
     */
    @TableField(value = "create_by")
    @ApiModelProperty(value="创建者")
    private String createBy;

    /**
     * 更新者
     */
    @TableField(value = "update_by")
    @ApiModelProperty(value="更新者")
    private String updateBy;

    public static final String COL_ITEM_ID = "item_id";

    public static final String COL_CHECK_ITEM_ID = "check_item_id";

    public static final String COL_CHECK_ITEM_NAME = "check_item_name";

    public static final String COL_PRICE = "price";

    public static final String COL_RESULT_MSG = "result_msg";

    public static final String COL_RESULT_IMG = "result_img";

    public static final String COL_PATIENT_ID = "patient_id";

    public static final String COL_PATIENT_NAME = "patient_name";

    public static final String COL_RESULT_STATUS = "result_status";

    public static final String COL_REG_ID = "reg_id";

    public static final String COL_CREATE_TIME = "create_time";

    public static final String COL_UPDATE_TIME = "update_time";

    public static final String COL_CREATE_BY = "create_by";

    public static final String COL_UPDATE_BY = "update_by";
}