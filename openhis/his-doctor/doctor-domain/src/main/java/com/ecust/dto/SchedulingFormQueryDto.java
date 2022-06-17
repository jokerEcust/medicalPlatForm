package com.ecust.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.servlet.Servlet;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

@ApiModel(value="com-ecust-dto-SchedulingFormQueryDto")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SchedulingFormQueryDto implements Serializable{
    private String beginDate;
    private List<SchedulingData> data;
    @Data
    public static class SchedulingData implements Serializable{
        private Long userId;
        private Long deptId;
        private String subsectionType;
        private Collection<String> schedulingType;
    }
}
