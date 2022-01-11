package com.excelsecu.iot.client;

import com.excelsecu.iot.*;
import com.excelsecu.iot.crypto.SignType;
import com.excelsecu.iot.crypto.Signature;
import com.excelsecu.iot.util.HttpClientUtil;
import com.excelsecu.iot.util.JsonUtil;
import com.excelsecu.iot.util.SystemClock;
import com.excelsecu.iot.util.WebUtil;
import org.apache.hc.core5.http.HttpHost;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author sekfung
 * @date 2021/12/31
 */
public class EsIoTClientV2 implements EsClient<EsRequestV2, EsResponse> {
    private static final Logger log = LoggerFactory.getLogger(EsIoTClientV1.class);

    private String priKey;
    private String pubKey;
    private SignType signType;
    private Map<String, String> headers;


    public EsIoTClientV2(String priKey, String pubKey, SignType signType) {
        this.signType = signType;
        this.priKey = priKey;
        this.pubKey = pubKey;
        this.headers = new HashMap<>();
    }

    private Map prepareParams(EsRequest<?> request) throws EsIoTApiException {
        Map params = JsonUtil.toObject(Map.class, request);
        params.put("reqTime", SystemClock.nowString());
        params.put("reqId", UUID.randomUUID().toString().replace("-", ""));
        return params;
    }

    private String prepareAuthorization(Map<String, String> params) throws EsIoTApiException {
        String strToSign = WebUtil.buildOrderSignStr(params);
        String sign = Signature.sign(strToSign, this.priKey, signType);
        String authType = "";
        if (SignType.RSA2.equals(signType)) {
            authType = "ESIOT-SHA256-RSA2048";
        }
        // 空格隔开
        // Authorization: 认证类型 签名信息
        return authType + " " + sign;
    }

    private Map<String, String> prepareHeaders(Map<String, String> params) throws EsIoTApiException {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", this.prepareAuthorization(params));
        return headers;
    }


    @Override
    public void setProxy(String proxyIp, int proxyPort) {
        HttpClientUtil.setProxy(proxyIp, proxyPort);
    }

    @Override
    public void setHTTPSProxy(String proxyIp, int proxyPort) {
        HttpClientUtil.setHttpsProxy(proxyIp, proxyPort);
    }

    @Override
    public void setProxy(HttpHost httpHost) {
        HttpClientUtil.setProxy(httpHost);
    }


    @Override
    public <R extends EsResponse> R execute(EsRequestV2 request) throws EsIoTApiException {
        String respStr = "";
        Map<String, String> params = this.prepareParams(request);
        Map<String, String> headers = this.prepareHeaders(params);
        if (EsRequest.Method.POST.equalsIgnoreCase(request.getMethod())) {
            respStr = WebUtil.doPost(request.getRequestPath(), params, headers);
        }
        return (R) JsonUtil.toObject(request.getResponseClass(), respStr);
    }

    @Override
    public <R extends EsResponse> void asyncExecute(EsRequestV2 request, EsIoTApiCallback<R> callback) throws EsIoTApiException {
        String respStr = "";
        Map<String, String> params = this.prepareParams(request);
        Map<String, String> headers = this.prepareHeaders(params);
        EsIoTApiCallback<String> callbackWrapper = new EsIoTApiCallback<String>() {
            @Override
            public void completed(String data) {
                callback.completed((R) JsonUtil.toObject(request.getResponseClass(), data));
            }

            @Override
            public void failed(Exception e) {
                log.error("execute request failed.", e);
                callback.failed(e);
            }
        };
        if (EsRequest.Method.POST.equalsIgnoreCase(request.getMethod())) {
            WebUtil.doPost(request.getRequestPath(), params, headers, callbackWrapper);
        }
    }

}
