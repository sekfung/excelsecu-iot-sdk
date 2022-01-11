package com.excelsecu.iot.request;

import com.excelsecu.iot.EsIoTApiException;
import com.excelsecu.iot.client.EsIoTClientV1;
import com.excelsecu.iot.type.PayType;
import com.excelsecu.iot.type.TransactionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author sekfung
 * @date 2022/1/4
 */
public class MessageRequestV1Test {

    private String appId = "esexcelsecu";
    private String secret = "FwkjiXzxzazZEc6hWzkm";
    private EsIoTClientV1 ioTClient;

    @BeforeEach
    public void init() {
        ioTClient = new EsIoTClientV1(secret);
    }

    @Test
    public void execute() {
        MessageRequestV1 requestV1 = MessageRequestV1.builder()
                .deviceId("802000000024")
                .transType(TransactionType.PAY)
                .txnAmt("1.01")
                .payType(PayType.AliPay)
                .build();

        MessageRequestV1 request = MessageRequestV1.builder()
                .deviceId("802000000024")
                .transType(TransactionType.from("01"))
                .txnAmt("1.01")
                .payType(PayType.AliPay)
                .build();
        try {
            ioTClient.execute(requestV1);
        } catch (EsIoTApiException e) {
            e.printStackTrace();
        }
    }
}