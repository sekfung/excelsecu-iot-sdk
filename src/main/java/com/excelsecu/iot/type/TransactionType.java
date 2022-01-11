package com.excelsecu.iot.type;

/**
 * 交易类型枚举
 *
 * @author sekfung
 * @date 2022/1/5
 */
public enum TransactionType {
    PAY("01", "收款"),
    Refund("02", "退款");

    TransactionType(String type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    private String type;
    private String desc;

    public String getDesc() {
        return desc;
    }

    public String getType() {
        return type;
    }

    public static TransactionType from(String type) {
        for (TransactionType value : TransactionType.values()) {
            if (value.getType().equalsIgnoreCase(type)) {
                return value;
            }
        }
        return TransactionType.PAY;
    }
}
