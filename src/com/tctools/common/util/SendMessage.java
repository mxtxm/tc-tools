package com.tctools.common.util;

import com.tctools.business.dto.user.User;
import com.vantar.exception.HttpException;
import com.vantar.http.*;
import com.vantar.locale.LangKey;
import org.slf4j.*;
import java.util.*;


public class SendMessage {

    private static final Logger log = LoggerFactory.getLogger(SendMessage.class);
    private static final String SMS_URL = "http://api.ghasedaksms.com/v2/sms/send/bulk2";
    private static final String SMS_API_KEY = "XbKl5CDPEGlkqLG7P7r1ePaRo+4i43oAfv/+6NN2yVs";
    private static final String SMS_SENDER = "2188930150";


    public static void sendSms(String mobile, String message) {
        Map<String, Object> params = new HashMap<>();
        params.put("message", message);
        params.put("sender", SMS_SENDER);
        params.put("receptor", mobile);
        HttpConnect connect = new HttpConnect();
        try {
            HttpResponse response = connect
                .addHeader("apikey", SMS_API_KEY)
                .addHeader("cache-control", "no-cache")
                .addHeader("content-type", "application/x-www-form-urlencoded")
                .post(SMS_URL, params);

            log.info("SMS API > {}", response.toString());

        } catch (HttpException e) {
            log.error("! {} > {}", message, mobile, e);
        }
    }

    public static void sendEmail(User user, LangKey key, String... params) {

    }

}