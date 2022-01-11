package com.excelsecu.iot.serialize;

import com.excelsecu.iot.type.TransactionType;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * @author sekfung
 * @date 2022/1/5
 */
public class TransactionTypeSerialize extends JsonSerializer<TransactionType> {
    @Override
    public void serialize(TransactionType transactionType, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeString(transactionType.getType());
    }
}
