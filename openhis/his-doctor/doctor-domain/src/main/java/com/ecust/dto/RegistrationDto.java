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

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.Date;

@ApiModel(value="com-ecust-dto-RegistrationDto")
@Data
@EqualsAndHashCode(callSuper=true)
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationDto extends BaseDto {
    /**
     * 挂号流水
     */
    @ApiModelProperty(value="挂号流水")
    private String regId;

    /**
     * 患者ID
     */
    @ApiModelProperty(value="患者ID")
    private String patientId;

    /**
     * 患者姓名
     */
    @ApiModelProperty(value="患者姓名")
    private String patientName;

    /**
     * 接诊医生ID
     */
    @ApiModelProperty(value="接诊医生ID")
    private Long userId;

    /**
     * 接诊医生姓名
     */
    @ApiModelProperty(value="接诊医生姓名")
    private String doctorName;

    /**
     * 科室ID
     */
    @ApiModelProperty(value="科室ID")
    private Long deptId;

    /**
     * 挂号费用ID
     */
    @ApiModelProperty(value="挂号费用ID")
    private Long regItemId;

    /**
     * 挂号总金额
     */
    @ApiModelProperty(value="挂号总金额")
    private BigDecimal regItemAmount;

    /**
     * 挂号编号
     */
    @ApiModelProperty(value="挂号编号")
    private Integer regNumber;

    /**
     * 挂号状态0未收费,1待就诊，2,就诊中，3，就诊完成，4，已退号，5 作废
     */
    @ApiModelProperty(value="挂号状态0未收费,1待就诊，2,就诊中，3，就诊完成，4，已退号，5 作废")
    private String regStatus;

    /**
     * 就诊日期
     */
    @NotBlank(message = "就诊日期不能为空")
    @ApiModelProperty(value="就诊日期")
    private String visitDate;

    /**
     * 排班类型1 门诊 2 急诊 字典表数据翻译
     */
    @ApiModelProperty(value="排班类型1 门诊 2 急诊 字典表数据翻译")
    private String schedulingType;

    /**
     * 排班时段1上午  2下午 3晚上 字典表数据翻译
     */
    @ApiModelProperty(value="排班时段1上午  2下午 3晚上 字典表数据翻译")
    private String subsectionType;
}