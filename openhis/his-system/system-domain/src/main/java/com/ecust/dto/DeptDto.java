package com.ecust.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * 科室传输实体
 */
@ApiModel(value = "com-ecust-domain-DeptDto")
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class DeptDto extends BaseDto {

    /**
     * 部门科室id
     */
    @ApiModelProperty(value="部门科室id")
    private Long deptId;
    /**
     * 部门名称
     */
    @NotNull(message = "部门名称不能为空")
    @ApiModelProperty(value = "部门名称")
    private String deptName;

    /**
     * 挂号编号
     */
    @NotNull(message = "挂号编号不能为空")
    @ApiModelProperty(value = "挂号编号")
    private Integer regNumber;

    /**
     * 科室编号
     */
    @NotNull(message = "科室编号不能为空")
    @ApiModelProperty(value = "科室编号")
    private String deptNumber;

    /**
     * 显示顺序
     */
    @ApiModelProperty(value = "显示顺序")
    private Integer orderNum;


    /**
     * 部门状态（0正常 1停用）
     */
    @NotNull(message = "部门状态不能为空，且只能为0或1")
//    @Pattern("^[0-1]*$")
    @ApiModelProperty(value = "部门状态（0正常 1停用）")
    private String status;

    /*** 负责人 */
    @ApiModelProperty(value = "负责人")
    private String deptLeader;
    /*** 联系电话 */
    @ApiModelProperty(value = "联系电话")
    private String leaderPhone;

}
