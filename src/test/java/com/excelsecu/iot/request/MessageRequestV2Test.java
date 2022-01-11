package com.excelsecu.iot.request;

import com.excelsecu.iot.EsIoTApiException;
import com.excelsecu.iot.EsIoTApiCallback;
import com.excelsecu.iot.client.EsIoTClientV2;
import com.excelsecu.iot.crypto.SignType;
import com.excelsecu.iot.response.MessageResponseV2;
import com.excelsecu.iot.type.PayType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author sekfung
 * @date 2022/1/5
 */
class MessageRequestV2Test {

    private EsIoTClientV2 ioTClientV2;

    private String pubKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAvCFh49JlqhjX5ySgowFbyGpgMLegEucRR23+829vVRbsGGITQfxBlRrOI7srAn1M/RZt7ZxonrHF9oNi/54lo9RsfnV3bE6Mp1p9JnbeIwLCE0zNy15M9xXVWY7C/pbAPqXZWN4XzlkTGRPB3nzoTYibMaIdGV8r8qDrZtfyiet/sPdPxK64xaEFMeVg3XvtdwX5MQiFd2SfRll2Zbwy7xNtZhN+mMNa09ZUURJr98HDF0xzfHqd67xtPXHL+a1IejiWgglq8LIec1wVoULMX1tRtw5LRKg0RCeX8NDQ0Pq+b/RPe9Ct1gNRipI1CE070M4dqRVcR22G0z9qw0meZwIDAQAB";
    private String priKey = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQC8IWHj0mWqGNfnJKCjAVvIamAwt6AS5xFHbf7zb29VFuwYYhNB/EGVGs4juysCfUz9Fm3tnGiescX2g2L/niWj1Gx+dXdsToynWn0mdt4jAsITTM3LXkz3FdVZjsL+lsA+pdlY3hfOWRMZE8HefOhNiJsxoh0ZXyvyoOtm1/KJ63+w90/ErrjFoQUx5WDde+13BfkxCIV3ZJ9GWXZlvDLvE21mE36Yw1rT1lRREmv3wcMXTHN8ep3rvG09ccv5rUh6OJaCCWrwsh5zXBWhQsxfW1G3DktEqDREJ5fw0NDQ+r5v9E970K3WA1GKkjUITTvQzh2pFVxHbYbTP2rDSZ5nAgMBAAECggEAQgMkqiAldC4LdBuyEgBkCErrPazxCjDyKzI5h4nOcvj8FwIy57BwArXJJ1iBmvoEjbnVEM8VFroiUIGBt1fytS3Qc/8bzHEzgXWK/HgxMe/B0avdekK4luqeVjJ10YdsJY9kWH51s/4laWM5X6xMhwgsDJppw4FVlmlIsGmNvT6+GcMbaIJpKL20z0/knrh0lhS0G8nrjnMQLVLNG8h8yKK0J831rZO93WbtCeT/yWeYQrs01wfkaA8MnnwiFXtAAsLvX3oxpIq3Zj6YXuoRecAq4KSail46Dt0gxTPDmWfQI6tUTGtsSjsHxAl3szh35IvD9VzNebdn/d6cW/rVwQKBgQD2vLTfurpbc8uftTp82yZdHwlbO9ZQR/C0L5AfOK7jeWlIubpFT63lqYjRkPfWzRkfOwhscQw193XXKjXRoZ+pJVWWLOdMwsKXW8ua1BPbF2gnjXxmYGL9KOIdfzLAdBe5aI0XlbNiL9ktq5hll65oHS3bs1tLOGCKSGkeMjczaQKBgQDDMW35dg1IT6oNTqknnsyCguYa8lLa64QkcE1X53qrIiINZqLnT9YkmULuGhNC2XGgrETVdVvX8kcoVmBaJ0dQ5IzyUszgQMSDS91DkXEysmbM/2gAPoXpb1ok4HID7RRuH8+vVGrE1wVhjgHq6C4h+SPs/CzVAUgDeYfbnG+ZTwKBgQC+vRy6Q9iir/5+pRPkx2l93WtXOwevIh4CvS4V5LTWJtpsHR/RSwpsm0WodAAT7pN4ICOf6Th+U6GmRvw55U7O7IygPnBEV98kMRNRypba7NpYkTjUz7hWao96LAWo31Yjv54q2FBHHCQw+REjoEF0PPJhf+KYYKnI3LdeX4Vq2QKBgFPVwxpF7eaE8NDOXDC8L7JObm5cbeHBDfxoj4+VyEYAXwfKcWVQEPWfLfP+Fw+7POZ5lwbnZQ8W7CETGEcgX60KvM7HaV9X8g9CYkJHEYtovGaHvC9+qx1byifUjqPJC+FQoG75nvH0OEdyKriuCEJnM1rx5H+IEVoMtKFKS1RZAoGAQglXXPN6PIBjx7ETcMKnj9mBxuN2WaVPC2fo1al9dRLgNXbxTopisyS3RQKRCJWZk31bL0D4D6C9x8ZvvVnugElqboOljp8wEsvb9/YUDWDNuKskgn7C2+wF0qgD5TsJCyIinVmICW4phhtQUFkMfG7/7rIU0SPqHLDKR40Gr4k=";

    private String appId = "esexcelsecu";

    @BeforeEach
    public void init() {
        ioTClientV2 = new EsIoTClientV2(priKey,  pubKey, SignType.RSA2);
    }

    @Test
    public void execute() {
        MessageRequestV2 requestV2 = MessageRequestV2.builder()
                .appId("esexcelsecu")
                .deviceId("802000000024")
                .txnAmt("123456")
                .payType(PayType.AliPay)
                .build();

        MessageRequestV2 request = MessageRequestV2.builder()
                .appId("esexcelsecu")
                .deviceId("669900000068")
                .txnAmt("1.01")
                .payType(PayType.from("1"))
                .build();
        try {
            ioTClientV2.execute(request);
        } catch (EsIoTApiException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void asyncExecute() {
        MessageRequestV2 requestV2 = MessageRequestV2.builder()
                .appId("esexcelsecu")
                .deviceId("")
                .txnAmt("1.01")
                .payType(PayType.AliPay)
                .build();

        try {
            ioTClientV2.asyncExecute(requestV2, new EsIoTApiCallback<MessageResponseV2>() {
                @Override
                public void completed(MessageResponseV2 data) {
                    System.out.println(data.getRespCode());
                    System.out.println(data.getData());
                    System.out.println(data.isSuccess());
                }

                @Override
                public void failed(Exception e) {

                }
            });
        } catch (EsIoTApiException e) {
            e.printStackTrace();
        }
    }
}