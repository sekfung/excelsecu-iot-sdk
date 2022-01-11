package com.excelsecu.iot.request;

import com.excelsecu.iot.EsRequestV1;
import com.excelsecu.iot.serialize.PayTypeSerialize;
import com.excelsecu.iot.serialize.TransactionTypeSerialize;
import com.excelsecu.iot.response.MessageResponseV1;
import com.excelsecu.iot.type.PayType;
import com.excelsecu.iot.type.TransactionType;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Builder;

/**
 * @author sekfung
 * @date 2022/1/4
 */
@Builder
public class MessageRequestV1 implements EsRequestV1<MessageResponseV1> {
    /**
     * 应用ID
     */
    @JsonProperty(value = "appId")
    private String appId;
    /**
     * 设备ID
     */
    @JsonProperty(value = "deviceId")
    private String deviceId;
    /**
     * 播报金额，单位元
     */
    @JsonProperty(value = "txnAmt")
    private String txnAmt;
    /**
     * 交易类型：01 收款 02 退款
     * 其他值情况视作01
     */
    @JsonProperty(value = "transType")
    @JsonSerialize(using = TransactionTypeSerialize.class)
    private TransactionType transType;
    /**
     * 支付类型
     */
    @JsonProperty(value = "payType")
    @JsonSerialize(using = PayTypeSerialize.class)
    private PayType payType;
    /**
     * 支付类型：
     * 01 微信支付
     * 02 支付宝
     * 07 蜀信e支付
     * 08 银联支付
     * 26 惠支付个人版支付
     * 29 (新)蜀信e支付
     * 目前暂时可不使用，统一播报“惠支付收款、退款”，后期若业务需求要求同时播报支付类型可使用，如“惠支付微信收款、惠支付蜀信e收款”
     */
    @JsonProperty(value = "sign")
    private String sign;

    @Override
    public String getRequestPath() {
        return "/api/v1/speaker/message";
    }

    @Override
    public String getMethod() {
        return Method.POST;
    }

    @Override
    public Class<MessageResponseV1> getResponseClass() {
        return MessageResponseV1.class;
    }
}
