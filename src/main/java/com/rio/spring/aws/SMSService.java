package com.rio.spring.aws;

/**
 * @author rioortizr
 *
 */
public interface SMSService
{
    String sendSMS( final String phoneNumber, final String message );
}
