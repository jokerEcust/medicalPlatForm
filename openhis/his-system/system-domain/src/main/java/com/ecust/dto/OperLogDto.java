package com.ecust.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;

@ApiModel(value="com-ecust-domain-OperLog")
@Data
@EqualsAndHashCode(callSuper=true)
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "sys_oper_log")
public class OperLogDto extends BaseDto {

    /**
     * 模块标题
     */
    @ApiModelProperty(value="模块标题")
    private String title;

    /**
     * 业务类型（0其它 1新增 2修改 3删除）
     */
    @ApiModelProperty(value="业务类型（0其它 1新增 2修改 3删除）")
    private String businessType;

    /**
     * 方法名称
     */
    @ApiModelProperty(value="方法名称")
    private String method;

    /**
     * 请求方式
     */
    @ApiModelProperty(value="请求方式")
    private String requestMethod;

    /**
     * 操作类别（0其它 1后台用户 2手机端用户）
     */
    @ApiModelProperty(value="操作类别（0其它 1后台用户 2手机端用户）")
    private Integer operatorType;

    /**
     * 操作人员
     */
    @ApiModelProperty(value="操作人员")
    private String operName;

    /**
     * 请求URL
     */
    @ApiModelProperty(value="请求URL")
    private String operUrl;

    /**
     * 主机地址
     */
    @ApiModelProperty(value="主机地址")
    private String operIp;

    /**
     * 操作地点
     */
    @ApiModelProperty(value="操作地点")
    private String operLocation;

    /**
     * 请求参数
     */
    @ApiModelProperty(value="请求参数")
    private String operParam;

    /**
     * 返回参数
     */
    @ApiModelProperty(value="返回参数")
    private String jsonResult;

    /**
     * 操作状态（0成功1失败）
     */
    @ApiModelProperty(value="操作状态（0成功1失败）")
    private String status;

    /**
     * 错误消息
     */
    @ApiModelProperty(value="错误消息")
    private String errorMsg;

    /**
     * 操作时间
     */
    @ApiModelProperty(value="操作时间")
    private Date operTime;
}
