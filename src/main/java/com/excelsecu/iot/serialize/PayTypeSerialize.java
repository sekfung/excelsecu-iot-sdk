package com.excelsecu.iot.serialize;

import com.excelsecu.iot.type.PayType;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * @author sekfung
 * @date 2022/1/5
 */
public class PayTypeSerialize extends JsonSerializer<PayType> {
    @Override
    public void serialize(PayType payType, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeString(payType.getType());
    }
}
