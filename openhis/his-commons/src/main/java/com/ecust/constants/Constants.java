package com.ecust.constants;

/**
 * 常量类
 * 存放项目中常用的常量
 */
public interface Constants {
    //定义成接口的目的：对于属性默认是public static final，方便使用
//     token：令牌
    String TOKEN = "token";
    //    用户的类型：0-管理员；1-普通用户
    String USER_ADMIN = "0";
    String USER_NORMAL = "1";
    //    返回的校验码：0-唯一；1-不唯一
    String UNIQUE = "0";
    String NOT_UNIQUE = "0";

    //    有效状态：0-true；1-false
    String STATUS_TRUE = "0";
    String STATUS_FALSE = "0";
    //    删除状态：1-未删除；0-已删除
    String DEL_FALSE = "0";
    String DEL_TRUE = "1";
    //    菜单类型：
    String MENU_TYPE_M = "M";
    String MENU_TYPE_C = "C";
    String MENU_TYPE_F = "F";
    //    入库订单状态：1-未提交；2-待审核；3-审核通过；4-审核失败；5-作废；6-入库成功
    String STOCK_PURCHASE_STATUS_1 = "1";
    String STOCK_PURCHASE_STATUS_2 = "2";
    String STOCK_PURCHASE_STATUS_3 = "3";
    String STOCK_PURCHASE_STATUS_4 = "4";
    String STOCK_PURCHASE_STATUS_5 = "5";
    String STOCK_PURCHASE_STATUS_6 = "6";
    //    入库状态
    String STOCK_STORAGE_0 = "0";
    String STOCK_STORAGE_1 = "1";
    //    默认预警值
    Long DEFAULT_WARNING = 50L;
    //    排班状态
    String SCHEDULING_FLAG_TRUE = "0";
    String SCHEDULING_FLAG_FALSE = "1";
    //    是否完善信息
    String IS_FINAL_FALSE = "0";
    String IS_FINAL_TRUE = "1";
    //    挂号单状态
    String REG_STATUS_0 = "0"; //待支付
    String REG_STATUS_1 = "1"; //待就诊
    String REG_STATUS_2 = "2"; //就诊中
    String REG_STATUS_3 = "3"; //就诊完成
    String REG_STATUS_4 = "4"; //已退号
    String REG_STATUS_5 = "5"; //已作废
    //    处方类型
    String CO_TYPE_MEDICINES = "0";
    String CO_TYPE_CHECK = "1";
    //    支付单状态
    String ORDER_STATUS_0 = "0";
    String ORDER_STATUS_1 = "1";
    String ORDER_STATUS_2 = "2";
    //    订单子项目支付状态
    String ORDER_DETAILS_STATUS_0 = "0";
    String ORDER_DETAILS_STATUS_1 = "1";
    String ORDER_DETAILS_STATUS_2 = "2";
    String ORDER_DETAILS_STATUS_3 = "3";
    //    检查状态
    String RESULT_STATUS_0 = "0";
    String RESULT_STATUS_1 = "1";
    //    退费单状态
    String ORDER_BACKFEE_STATUS_0 = "0";
    String ORDER_BACKFEE_STATUS_1 = "1";
    String ORDER_BACKFEE_STATUS_2 = "2";
    //    支付类型
    String PAY_TYPE_0 = "0";//现金
    String PAY_TYPE_1 = "1";//支付宝


    //redis前缀
    String DICT_REDIS_PROFIX="dict:";

    /**
     * 登陆状态
     * 0：成功；1：失败
     */
    String LOGIN_SUCCESS="0";
    String LOGIN_ERROR="1";

    /**
     * 登陆类型
     * 0：系统后台用户；1：患者用户
     */
    String LOGIN_TYPE_SYSTEM="0";
    String LOGIN_TYPE_PATIENT="1";

    //采购的前缀
    String ID_PROFIX_CG="CG";
    String ID_PROFIX_CH ="CH";//病历

    String ID_PROFIX_CO = "CO"; //病历
    String ID_PROFIX_ITEM = "ITEM"; //病历详情

    String ID_PROFIX_ODC = "ODC";
    String ID_PROFIX_ODB = "ODB";//退费订单前缀
}
