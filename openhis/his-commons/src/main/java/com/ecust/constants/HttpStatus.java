package com.ecust.constants;

public interface HttpStatus {
    // 成功 200；
    int SUCCESS = 200;
    // 参数列表错误：400；
    int BAD_REQUEST = 400;
    // 未授权：401；
    int UNAUTHORIZED = 401;
    // 权限过期：403；
    int FORBIDDEN = 403;
    // 资源未找到：404
    int NOT_FOUND = 404;
    // 不允许http方法：405；
    int BAD_METHOD = 405;
    // 系统内部错误：500
    int ERROR = 500;
}
