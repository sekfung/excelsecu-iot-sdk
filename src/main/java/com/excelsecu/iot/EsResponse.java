package com.excelsecu.iot;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author sekfung
 * @date 2022/1/4
 */
@Getter
@Setter
public class EsResponse {
    @JsonProperty(value = "respCode")
    protected String respCode;
    @JsonProperty(value = "respMsg")
    protected String respMsg;
    public boolean isSuccess() {
        return "0000".equalsIgnoreCase(respCode);
    }
}

