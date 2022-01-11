package com.excelsecu.iot;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author sekfung
 * @date 2021/12/31
 */
public interface EsRequest<T extends EsResponse> {

    @JsonIgnore
    String getRequestPath();
    @JsonIgnore
    String getMethod();
    @JsonIgnore
    Class<T> getResponseClass();



    class Method {
        public static final String GET = "GET";
        public static final String POST = "POST";
        public static final String PUT = "PUT";
        public static final String DELETE = "DELETE";
        public static final String PATCH = "PATCH";
    }

    class Scheme {
        public static final String HTTPS = "https";
        public static final String HTTP = "http";
    }
}
