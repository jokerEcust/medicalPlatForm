package com.ecust.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@ApiModel(value="com-ecust-domain-NoticeDto")
@Data
@EqualsAndHashCode(callSuper=true)
@AllArgsConstructor
@NoArgsConstructor
public class NoticeDto extends BaseDto {

    /**
     * 公告ID
     */
    @ApiModelProperty(value="公告ID")
    private Integer noticeId;

    /**
     * 公告标题
     */
    @NotBlank(message = "公告标题不能为空")
    @ApiModelProperty(value="公告标题")
    private String noticeTitle;

    /**
     * 公告类型（1通知 2公告）
     */
    @NotBlank(message = "公告类型不能为空")
    @ApiModelProperty(value="公告类型（1通知 2公告）")
    private String noticeType;

    /**
     * 公告内容
     */
    @NotBlank(message = "公告内容不能为空")
    @ApiModelProperty(value="公告内容")
    private String noticeContent;

    /**
     * 公告状态（0正常 1关闭）
     */
    @NotBlank(message = "公告状态不能为空")
    @ApiModelProperty(value="公告状态（0正常 1关闭）")
    private String status;

    /**
     * 备注
     */
    @ApiModelProperty(value="备注")
    private String remark;

    /**
     * 创建者
     */
    @TableField(value = "create_by")
    @ApiModelProperty(value="创建者")
    private String createBy;
}
