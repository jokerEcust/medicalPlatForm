package com.ecust.exception;


import com.ecust.vo.AjaxResult;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * 全局异常处理
     * 当出现MethodArgumentNotValidException异常时会进入到这个方法
     *
     * 数据优化
     * 返回的AjaxResult数据前端无法解析
     * 优化使得数据更加简洁
     * @param e
     * @return
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public AjaxResult jsonErrorHandler(MethodArgumentNotValidException e){
        List<Map<String,Object>> list=new ArrayList<>();
        List<ObjectError> allErrors = e.getBindingResult().getAllErrors();
        for (ObjectError allError : allErrors) {
            Map<String,Object> map=new HashMap<>();
            map.put("defaultMessage",allError.getDefaultMessage());
            map.put("ObjectName",allError.getObjectName());
//            map.put("field",allError.get)
            FieldError fieldError= (FieldError) allError;
            map.put("field",fieldError.getField());
            list.add(map);
        }
        return AjaxResult.fail("后端数据校验异常",list);
    }

    @ExceptionHandler(value = BindException.class)
    public AjaxResult otherErrorHandler(BindException e){
        List<Map<String,Object>> list=new ArrayList<>();
        List<ObjectError> allErrors = e.getBindingResult().getAllErrors();
        for (ObjectError allError : allErrors) {
            Map<String,Object> map=new HashMap<>();
            map.put("defaultMessage",allError.getDefaultMessage());
            map.put("ObjectName",allError.getObjectName());
//            map.put("field",allError.get)
            FieldError fieldError= (FieldError) allError;
            map.put("field",fieldError.getField());
            list.add(map);
        }
        return AjaxResult.fail("后端数据校验异常",list);
    }
}
