package com.excelsecu.iot.request;

import com.excelsecu.iot.EsRequestV2;
import com.excelsecu.iot.response.MessageResponseV2;
import com.excelsecu.iot.serialize.PayTypeSerialize;
import com.excelsecu.iot.type.PayType;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Builder;

/**
 * @author sekfung
 * @date 2021/12/31
 */
@Builder
public class MessageRequestV2 implements EsRequestV2<MessageResponseV2> {

    /**
     * 应用ID，需在智能物联平台注册应用获取
     */
    @JsonProperty(value = "appId")
    private String appId;
    /**
     * 设备ID
     */
    @JsonProperty(value = "deviceId")
    private String deviceId;
    /**
     * 播报金额
     */
    @JsonProperty(value = "txnAmt")
    private String txnAmt;
    /**
     * 支付类型
     * 1 数字货币收款
     * 2 支付宝收款
     * 3 微信收款
     * 4 银联云闪付收款
     * 5 银联刷卡收款
     * 6 会员卡消费收款
     * 7 会员卡充值
     * 8 翼支付收款
     * 9 退款
     * 10 支付宝退款
     * 11 微信退款
     * 12 银行卡退款
     * 13 银联退款
     * 14 工行e支付收款
     * 15 京东支付收款
     * 16 收款成功
     * #
     */
    @JsonProperty(value = "payType")
    @JsonSerialize(using = PayTypeSerialize.class)
    private PayType payType;

    @Override
    public String getRequestPath() {
        return "/api/v2/speaker/message";
    }

    @Override
    public String getMethod() {
        return Method.POST;
    }

    @Override
    public Class<MessageResponseV2> getResponseClass() {
        return MessageResponseV2.class;
    }
}
