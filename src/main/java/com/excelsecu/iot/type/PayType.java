package com.excelsecu.iot.type;

/**
 * ⽀付类型枚举
 * @author sekfung
 * @date 2022/1/5
 */
public enum PayType {
    DCEP("1", "数字货币收款"),
    AliPay("2", "支付宝收款"),
    WechatPay("3","微信收款"),
    UnionQuickPay("4", "银联云闪付收款"),
    UnionPos("5", "银联刷卡收款"),
    MembershipCardPay("6", "会员卡消费收款"),
    MembershipCardReCharge("7", "会员卡充值"),
    BestPay("8", "翼支付收款"),
    Refund("9", "退款"),
    AliRefund("10", "支付宝退款"),
    WechatRefund("11", "微信退款"),
    BankCardRefund("12", "银行卡退款"),
    UnionRefund("13", "银联退款"),
    ICBCEPay("14", "⼯⾏e⽀付收款"),
    JDPay("15", "京东⽀付收款"),
    PaySuccess("16", "收款成功");

    private String type;
    private String desc;

    PayType(String type, String desc) {
        this.type = type;
        this.desc = desc;
    }


    public String getDesc() {
        return desc;
    }

    public String getType() {
        return type;
    }

    public static PayType from(String type) {
        for (PayType value : PayType.values()) {
            if (value.getType().equals(type)) {
                return value;
            }
        }
        return PayType.PaySuccess;
    }
}
