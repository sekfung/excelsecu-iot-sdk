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

import java.util.Map;
import java.util.UUID;

/**
 * @author sekfung
 * @date 2022/1/4
 */
public class EsIoTClientV1 implements EsClient<EsRequestV1, EsResponse> {
    private static final Logger log = LoggerFactory.getLogger(EsIoTClientV1.class);

    private String secret;

    public EsIoTClientV1(String secret) {
        this.secret = secret;
    }

    private Map<String, String> prepareParams(EsRequest<?> request) throws EsIoTApiException {
        Map params = JsonUtil.toObject(Map.class, request);
        params.put("reqTime", SystemClock.nowString());
        params.put("reqId", UUID.randomUUID().toString().replace("-", ""));
        String strToSign = WebUtil.buildOrderSignStr(params);
        // V1版本固定签名算法为HMAC-SHA256
        String sign = Signature.sign(strToSign,  secret, SignType.HMAC_SHA256);
        params.put("sign", sign);
        return params;
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
    public <R extends EsResponse> R execute(EsRequestV1 request) throws EsIoTApiException {
        String respStr = "";
        Map<String, String> params = this.prepareParams(request);
        respStr = WebUtil.doPost(request.getRequestPath(), params);
        return (R) JsonUtil.toObject(request.getResponseClass(), respStr);
    }

    @Override
    public <R extends EsResponse> void asyncExecute(EsRequestV1 request, EsIoTApiCallback<R> callback) throws EsIoTApiException {
        String respStr = "";
        Map<String, String> params = this.prepareParams(request);
        EsIoTApiCallback callbackWrapper = new EsIoTApiCallback<String>() {
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
            WebUtil.doPost(request.getRequestPath(), params, callbackWrapper);
        }

    }


}
