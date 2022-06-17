package com.ecust.aspectj.annotation;

import com.ecust.aspectj.enums.BusinessType;
import com.ecust.aspectj.enums.OperatorType;

import java.lang.annotation.*;

@Target({ElementType.PARAMETER,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log {
    public String title() default "";

    /**
     * 业务操作类型：删除、修改、新增等
     * @return
     */
    public BusinessType businessType() default BusinessType.OTHER;
    public OperatorType operatorType() default OperatorType.MANAGER;
    public boolean isSaveRequestData() default true;
}
