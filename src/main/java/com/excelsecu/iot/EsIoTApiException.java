package com.excelsecu.iot;

/**
 * @author sekfung
 * @date 2021/12/31
 */
public class EsIoTApiException extends Exception{
    public EsIoTApiException(String msg) {
        super(msg);
    }

    public EsIoTApiException(String msg, Throwable throwable) {
        super(msg, throwable);
    }
}
