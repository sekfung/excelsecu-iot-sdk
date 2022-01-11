package com.excelsecu.iot;

/**
 * @author sekfung
 * @date 2022/1/4
 */
public interface EsIoTApiCallback<T> {
    void completed(T data);

    void failed(Exception e);
}
