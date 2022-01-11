package com.excelsecu.iot.util;

import com.excelsecu.iot.EsIoTApiCallback;
import com.excelsecu.iot.EsIoTApiException;
import com.excelsecu.iot.EsRequest;
import com.excelsecu.iot.client.EsIoTClientV1;
import org.apache.hc.client5.http.async.methods.SimpleHttpRequest;
import org.apache.hc.client5.http.async.methods.SimpleHttpResponse;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.classic.methods.HttpUriRequest;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.core5.concurrent.FutureCallback;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author sekfung
 * @date 2022/1/2
 */
public class WebUtil {
    private static final Logger log = LoggerFactory.getLogger(WebUtil.class);

    private static final String UA = "ESIOT-JAVA-SDK-CLIENT";
    private static final String server = "https://iot.excelsecu.com";


    public static String doPost(String url, Map<String, String> params) throws EsIoTApiException {
        return doPost(url, params, new HashMap<>());
    }

    public static String doPost(String url, Map<String, String> params, Map<String, String> headers) throws EsIoTApiException {
        return doPost(url, buildPostEntity(params), headers);
    }


    public static String doPost(String url, HttpEntity entity, Map<String, String> headers) throws EsIoTApiException {
        HttpPost httpPost = new HttpPost(server + url);
        httpPost.setEntity(entity);
        if (headers != null) {
            headers.put("user-agent", UA);
            addHeaders(httpPost, headers);
        }
       return execute(httpPost);
    }

    public static String execute(HttpUriRequest request) throws EsIoTApiException {
        CloseableHttpResponse resp;
        try {
            resp = HttpClientUtil.getSyncHttpClient().execute(request);
        } catch (IOException e) {
            log.error("execute request exception.", e);
            throw new EsIoTApiException("execute request exception.", e);
        }
        int statusCode = resp.getCode();
        HttpEntity entity = resp.getEntity();
        try {
            String respStr = EntityUtils.toString(entity);
            if (statusCode != 200) {
                log.error("response status code is not valid. status code: {}", statusCode);
                throw new EsIoTApiException("response status code is not valid. status code: " + statusCode + " resp is:" + respStr);
            }
            return respStr;
        } catch (IOException e) {
            log.error("io exception.", e);
            throw new EsIoTApiException("io exception.", e);
        } catch (ParseException e) {
            log.error("parse json exception.", e);
            throw new EsIoTApiException("parse json exception.", e);
        }
    }

    public static void execute(SimpleHttpRequest request, EsIoTApiCallback callback) throws EsIoTApiException {
        Future<SimpleHttpResponse> future = HttpClientUtil.getAsyncHttpClient().execute(request, new FutureCallback<SimpleHttpResponse>() {
            @Override
            public void completed(SimpleHttpResponse response) {
                int statusCode = response.getCode();
                String body = new String(response.getBody().getBodyBytes(), StandardCharsets.UTF_8);
                if (statusCode != 200) {
                    log.error("response status code is not valid. status code: {}", statusCode);
                    callback.failed(new EsIoTApiException("response status code is not valid. status code: " + statusCode + " resp is:" + body));
                }
                callback.completed(body);
            }

            @Override
            public void failed(Exception ex) {
                callback.failed(ex);
            }

            @Override
            public void cancelled() {
                callback.failed(new EsIoTApiException("async http client execute has been cancelled."));
            }
        });
        try {
            future.get(5, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException e) {
            callback.failed(new EsIoTApiException("async http client executed failed", e));
        } catch (TimeoutException e) {
            callback.failed(new EsIoTApiException("async http client executed timeout"));
        }
    }

    private static void addHeaders(HttpUriRequest request, Map<String, String> headers) {
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            String name = entry.getKey();
            String value = entry.getValue();
            if (StringsUtil.areNotEmpty(name, value)) {
                request.addHeader(name, value);
            }
        }

    }

    private static void addHeaders(SimpleHttpRequest request, Map<String, String> headers) {
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            String name = entry.getKey();
            String value = entry.getValue();
            if (StringsUtil.areNotEmpty(name, value)) {
                request.addHeader(name, value);
            }
        }
    }

    private static HttpEntity buildPostEntity(Map<String, String> params) throws EsIoTApiException {
        return new StringEntity(Objects.requireNonNull(JsonUtil.toJsonString(params)), ContentType.APPLICATION_JSON);
    }


    public static String buildOrderSignStr(Map<String, String> params) {
        Set<String> keySet = params.keySet();
        String[] keyArray = keySet.toArray(new String[0]);
        Arrays.sort(keyArray);
        StringBuilder sb = new StringBuilder();
        for (String k : keyArray) {
            if ("sign".equals(k)) {
                continue;
            }
            if (StringsUtil.isEmpty(params.get(k))) {
                params.remove(k);
                continue;
            }
            String value = params.get(k);
            if (String.valueOf(value).trim().length() > 0) {
                sb.append(k).append("=").append(String.valueOf(value).trim()).append("&");
            }
        }
        return sb.substring(0, sb.length() - 1);
    }

    public static void doPost(String url, Map<String, String> params, EsIoTApiCallback callback) throws EsIoTApiException {
        doPost(url, params, new HashMap<>(), callback);
    }

    public static void doPost(String url, Map<String, String> params, Map<String, String> headers, EsIoTApiCallback callback) throws EsIoTApiException {
        SimpleHttpRequest request = new SimpleHttpRequest(EsRequest.Method.POST, server + url);
        request.setBody(Objects.requireNonNull(JsonUtil.toJsonString(params)), ContentType.APPLICATION_JSON);
        if (headers != null) {
            headers.put("user-agent", UA);
            addHeaders(request, headers);
        }
        execute(request, callback);
    }

}
