package com.excelsecu.iot.util;

import com.excelsecu.iot.EsIoTApiException;
import com.excelsecu.iot.EsRequest;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.DefaultHttpRequestRetryStrategy;
import org.apache.hc.client5.http.impl.async.CloseableHttpAsyncClient;
import org.apache.hc.client5.http.impl.async.HttpAsyncClients;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.impl.nio.PoolingAsyncClientConnectionManager;
import org.apache.hc.client5.http.impl.nio.PoolingAsyncClientConnectionManagerBuilder;
import org.apache.hc.client5.http.ssl.ClientTlsStrategyBuilder;
import org.apache.hc.core5.http.ConnectionClosedException;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.HttpStatus;
import org.apache.hc.core5.http.io.SocketConfig;
import org.apache.hc.core5.http.nio.ssl.TlsStrategy;
import org.apache.hc.core5.http.ssl.TLS;
import org.apache.hc.core5.io.CloseMode;
import org.apache.hc.core5.reactor.IOReactorConfig;
import org.apache.hc.core5.ssl.SSLContexts;
import org.apache.hc.core5.util.TimeValue;
import org.apache.hc.core5.util.Timeout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLException;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.ConnectException;
import java.net.NoRouteToHostException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;

/**
 * @author sekfung
 * @date 2022/1/2
 */
public class HttpClientUtil {
    private static final Logger log = LoggerFactory.getLogger(HttpClientUtil.class);

    private static final int MAX_HTTP_CONNECTIONS = 200;
    private static final int DEFAULT_TIMEOUT = 5;

    private static volatile CloseableHttpAsyncClient asyncHttpClient = null;
    private static volatile CloseableHttpClient syncHttpClient = null;
    private static HttpHost proxyHost = null;

    public HttpClientUtil() {
    }

    private static void initSyncHttpClient() {
        log.info("[connection-timeout]=8000 [socket-timeout]=30000 [max-per-route]=" + MAX_HTTP_CONNECTIONS + " [max-total]=" + MAX_HTTP_CONNECTIONS + " config sync http client.");

        RequestConfig requestConfig;
        if (proxyHost == null) {
            requestConfig = RequestConfig.custom()
                    .setConnectTimeout(Timeout.ofSeconds(DEFAULT_TIMEOUT))
                    .setResponseTimeout(Timeout.ofSeconds(DEFAULT_TIMEOUT))
                    .build();
        } else {
            requestConfig = RequestConfig.custom()
                    .setConnectTimeout(Timeout.ofSeconds(DEFAULT_TIMEOUT))
                    .setResponseTimeout(Timeout.ofSeconds(DEFAULT_TIMEOUT))
                    .setProxy(proxyHost)
                    .build();
        }
        SocketConfig socketConfig = SocketConfig.custom().setSoKeepAlive(false).setSoReuseAddress(true).setTcpNoDelay(true).build();
        PoolingHttpClientConnectionManager cm = PoolingHttpClientConnectionManagerBuilder
                .create()
                .setMaxConnTotal(MAX_HTTP_CONNECTIONS)
                .setMaxConnTotal(MAX_HTTP_CONNECTIONS)
                .setDefaultSocketConfig(socketConfig)
                .build();
        syncHttpClient = HttpClients.custom().
                setDefaultRequestConfig(requestConfig)
                .evictExpiredConnections()
                .setRetryStrategy(new HttpRequestRetryStrategy())
                .setConnectionManager(cm)
                .setConnectionManagerShared(false)
                .build();

        log.info("add shut down hook to release http connection pool and close sync http client.");
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (syncHttpClient != null) {
                syncHttpClient.close(CloseMode.GRACEFUL);
            }
        }));
    }

    private static void initAsyncHttpClient() {
        log.info("[connection-timeout]=8000 [socket-timeout]=30000 [max-per-route]=" + MAX_HTTP_CONNECTIONS + " [max-total]=" + MAX_HTTP_CONNECTIONS + " config async http client.");
        RequestConfig requestConfig;
        if (proxyHost == null) {
            requestConfig = RequestConfig.custom()
                    .setConnectTimeout(Timeout.ofSeconds(DEFAULT_TIMEOUT))
                    .build();
        } else {
            requestConfig = RequestConfig.custom()
                    .setConnectTimeout(Timeout.ofSeconds(DEFAULT_TIMEOUT))
                    .setResponseTimeout(Timeout.ofSeconds(DEFAULT_TIMEOUT))
                    .setProxy(proxyHost)
                    .build();
        }
        IOReactorConfig ioReactorConfig = IOReactorConfig.custom()
                .setSoTimeout(Timeout.ofSeconds(DEFAULT_TIMEOUT))
                .setTcpNoDelay(true)
                .setSoKeepAlive(false)
                .setSoReuseAddress(true)
                .build();

        TlsStrategy tlsStrategy = ClientTlsStrategyBuilder.create()
                .setSslContext(SSLContexts.createSystemDefault())
                .setTlsVersions(TLS.V_1_2)
                .build();

        PoolingAsyncClientConnectionManager cm = PoolingAsyncClientConnectionManagerBuilder.create()
                .setMaxConnPerRoute(MAX_HTTP_CONNECTIONS)
                .setTlsStrategy(tlsStrategy)
                .setMaxConnTotal(MAX_HTTP_CONNECTIONS)
                .build();
        asyncHttpClient = HttpAsyncClients.custom().
                setDefaultRequestConfig(requestConfig)
                .setIOReactorConfig(ioReactorConfig)
                .setConnectionManager(cm)
                .setRetryStrategy(new HttpRequestRetryStrategy())
                .evictExpiredConnections()
                .setConnectionManagerShared(false)
                .build();
        asyncHttpClient.start();
        log.info("add shut down hook to release http connection pool and close async http client.");
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (asyncHttpClient != null) {
                asyncHttpClient.close(CloseMode.GRACEFUL);
            }
        }));
    }

    public static CloseableHttpClient getSyncHttpClient() {
        if (syncHttpClient == null) {
            synchronized (HttpClientUtil.class) {
                if (syncHttpClient == null) {
                    initSyncHttpClient();
                }
            }
        } else {
            return syncHttpClient;
        }
        return syncHttpClient;
    }

    public static CloseableHttpAsyncClient getAsyncHttpClient() throws EsIoTApiException {
        if (asyncHttpClient == null) {
            synchronized (HttpClientUtil.class) {
                if (asyncHttpClient == null) {
                    initAsyncHttpClient();
                }
            }
        } else {
            return asyncHttpClient;
        }
        return asyncHttpClient;
    }

    public static void setProxy(String proxyIp, int proxyPort) {
        proxyHost = new HttpHost(EsRequest.Scheme.HTTP, proxyIp, proxyPort);
    }

    public static void setHttpsProxy(String proxyIp, int proxyPort) {
        proxyHost = new HttpHost(EsRequest.Scheme.HTTPS, proxyIp, proxyPort);
    }

    public static void setProxy(HttpHost proxy) {
        proxyHost = proxy;
    }

    public static HttpHost getProxy() {
        return proxyHost;
    }

    private static class HttpRequestRetryStrategy extends DefaultHttpRequestRetryStrategy {

        public HttpRequestRetryStrategy() {
            this(3, TimeValue.ofSeconds(1));
        }

        public HttpRequestRetryStrategy(
                final int maxRetries,
                final TimeValue defaultRetryInterval) {
            this(maxRetries, defaultRetryInterval,
                    Arrays.asList(
                            InterruptedIOException.class,
                            UnknownHostException.class,
                            ConnectException.class,
                            ConnectionClosedException.class,
                            NoRouteToHostException.class,
                            SSLException.class),
                    Arrays.asList(
                            HttpStatus.SC_INTERNAL_SERVER_ERROR,
                            HttpStatus.SC_TOO_MANY_REQUESTS,
                            HttpStatus.SC_SERVICE_UNAVAILABLE));
        }

        public HttpRequestRetryStrategy(int maxRetries, TimeValue defaultRetryInterval, List<Class<? extends IOException>> clazzes, List<Integer> cnodes) {
            super(maxRetries, defaultRetryInterval, clazzes, cnodes);
        }
    }
}
