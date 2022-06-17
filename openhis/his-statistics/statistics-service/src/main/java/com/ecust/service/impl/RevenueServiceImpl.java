package com.ecust.service.impl;

import com.ecust.constants.Constants;
import com.ecust.domain.Income;
import com.ecust.domain.Refund;
import com.ecust.dto.RevenueQueryDto;
import com.ecust.mapper.RevenueMapper;
import com.ecust.service.RevenueService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class RevenueServiceImpl implements RevenueService {
    @Autowired
    private RevenueMapper revenueMapper;

    @Override
    public Map<String, Object> queryAllRevenueData(RevenueQueryDto revenueQueryDto) {
        Map<String, Object> map = new HashMap<>();
        //查询收入的
        List<Income> incomes = this.revenueMapper.queryIncome(revenueQueryDto);
        // 查询退费的
        List<Refund> refunds = this.revenueMapper.queryRefund(revenueQueryDto);
        //声明需要的数据对象
        Double totalRevenue = 0.00; //合计收入
        Double toll = 0.00;//总收入
        Double refund = 0.00;//总退费
        Double cashIncome = 0.00;//现金支付
        Double alipayIncome = 0.00;//支付宝支付
        Double cashRefund = 0.00;//现金退费
        Double alipayRefund = 0.00;//支付宝退费
        Integer incomeChanelCash = 0;//现金收取次数
        Integer incomeChanelAlipay = 0;//支付宝收取的次数

        for (Income income : incomes) {
            toll += income.getOrderAmount();
            if (income.getPayType().equals(Constants.PAY_TYPE_0)) {
                //现金支付
                cashIncome += income.getOrderAmount();
            } else {
                alipayIncome += income.getOrderAmount();
            }
        }

        for (Refund refund1 : refunds) {
            refund += refund1.getBackAmount();
            if (refund1.getBackType().equals(Constants.PAY_TYPE_0)) {
                cashRefund += refund1.getBackAmount();
            } else {
                alipayRefund += refund1.getBackAmount();
            }
        }
        totalRevenue = toll - refund;
        Map<String, Object> revenueObj = new HashMap<>();
        Map<String, Object> overview = new HashMap<>();
        overview.put("toll", toll);
        overview.put("refund", refund);
        Map<String, Object> channel = new HashMap<>();
        channel.put("cashRefund", cashRefund);
        channel.put("alipayRefund", alipayRefund);
        channel.put("cashIncome", cashIncome);
        channel.put("alipayIncome", alipayIncome);
        revenueObj.put("channel", channel);
        revenueObj.put("overview", overview);
        revenueObj.put("totalRevenue", totalRevenue);

        map.put("revenueObj", revenueObj);
        /************************************************/
        Map<String, Object> revenueOverview = new HashMap<>();
        revenueOverview.put("title", "收支概况");
        List<Map<String, Object>> revenueOverviewData = new ArrayList<>();
        Map<String, Object> revenueOverviewData1 = new HashMap<>();
        revenueOverviewData1.put("name", "收费金额");
        revenueOverviewData1.put("value", totalRevenue);
        Map<String, Object> revenueOverviewData2 = new HashMap<>();
        revenueOverviewData2.put("name", "退费金额");
        revenueOverviewData2.put("value", refund);
        revenueOverviewData.add(revenueOverviewData1);
        revenueOverviewData.add(revenueOverviewData2);
        revenueOverview.put("data", revenueOverviewData);

        map.put("revenueOverview", revenueOverview);
        /***********************************************/
        Map<String, Object> incomeChannel = new HashMap<>();
        incomeChannel.put("title", "收入渠道");
        List<Map<String, Object>> incomeChanelData = new ArrayList<>();
        Map<String, Object> incomeChanelData1 = new HashMap<>();
        incomeChanelData1.put("name", "现金笔数");
        incomeChanelData1.put("value", incomeChanelCash);
        Map<String, Object> incomeChanelData2 = new HashMap<>();
        incomeChanelData2.put("name", "支付宝笔数");
        incomeChanelData2.put("value", incomeChanelAlipay);
        incomeChanelData.add(incomeChanelData1);
        incomeChanelData.add(incomeChanelData2);
        incomeChannel.put("data", incomeChanelData);
        //放到返回的 map 里面
        map.put("incomeChanel", incomeChannel);
        /********************************************/
        Map<String, Object> refundMap = new HashMap<>();
        refundMap.put("title", "退款");
        List<Map<String, Object>> refundMapData = new ArrayList<>();
        Map<String, Object> refundMapData1 = new HashMap<>();
        refundMapData1.put("name", "现金退款");
        refundMapData1.put("value", cashRefund);
        Map<String, Object> refundMapData2 = new HashMap<>();
        refundMapData2.put("name", "支付宝退款");
        refundMapData2.put("value", alipayRefund);
        refundMapData.add(refundMapData1);
        refundMapData.add(refundMapData2);
        refundMap.put("data", refundMapData);

        map.put("refund", refundMap);
        return map;


    }
}
