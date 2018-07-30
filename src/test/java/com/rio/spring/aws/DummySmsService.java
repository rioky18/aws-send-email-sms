package com.rio.spring.aws;

import com.rio.spring.aws.SMSService;

public class DummySmsService implements SMSService
{
    private final String messageId = "123456";
    
    @Override
    public String sendSMS( String phoneNumber, String message )
    {
        return messageId;
    }

}
