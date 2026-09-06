package com.aurelia.banking.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TwilioSmsService {

    @Value("${TWILIO_ACCOUNT_SID}")
    private String accountSid;

    @Value("${TWILIO_AUTH_TOKEN}")
    private String authToken;

    @Value("${TWILIO_PHONE_NUMBER}")
    private String fromNumber;


    public void sendOtp(
            String phoneNumber,
            String otp
    ) {

        Twilio.init(
                accountSid,
                authToken
        );

        Message.creator(

                new PhoneNumber(
                        phoneNumber
                ),

                new PhoneNumber(
                        fromNumber
                ),

                "sms_2fa"

        ).create();
    }
}