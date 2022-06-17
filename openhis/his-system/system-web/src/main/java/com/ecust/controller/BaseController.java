package com.ecust.controller;

import com.ecust.vo.AjaxResult;
import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;

/**
 * 对于调用外部服务的控制器提供熔断措施
 */
@DefaultProperties(defaultFallback = "fallback")
public class BaseController {
    public AjaxResult fallback(){
        return AjaxResult.toAjax(-1);
    }
}
