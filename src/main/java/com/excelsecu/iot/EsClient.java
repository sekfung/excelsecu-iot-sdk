package com.excelsecu.iot;

import org.apache.hc.core5.http.HttpHost;

/**
 * @author sekfung
 * @date 2021/12/31
 */
public interface EsClient<T extends EsRequest, R extends EsResponse> {
    /**
     * HTTP proxy
     * @param proxyIp
     * @param proxyPort
     */
    void setProxy(String proxyIp, int proxyPort);

    /**
     * HTTPs Proxy
     * @param proxyIp
     * @param proxyPort
     */
    void setHTTPSProxy(String proxyIp, int proxyPort);

    /**
     * HTTP Proxy
     * @param httpHost
     */
    void setProxy(HttpHost httpHost);
    <R extends EsResponse> R execute(T request) throws EsIoTApiException;
    <R extends EsResponse> void asyncExecute(T request, EsIoTApiCallback<R> callback) throws EsIoTApiException;
}
