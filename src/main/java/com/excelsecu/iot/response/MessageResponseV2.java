package com.excelsecu.iot.response;

import com.excelsecu.iot.EsResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Collections;
import java.util.List;

/**
 * @author sekfung
 * @date 2021/12/31
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MessageResponseV2 extends EsResponse {
    @JsonProperty(value = "data")
    private Object data;
}
