package com.rio.spring.aws;

import com.rio.spring.aws.EmailService;
import com.rio.spring.aws.MailException;
import com.rio.spring.aws.SimpleMailMessage;

public class DummyEmailService implements EmailService
{

    private final String messageId = "123456";
    
    @Override
    public String send( SimpleMailMessage email ) throws MailException
    {
        return messageId;
    }

	@Override
	public String send(String from, String[] to, String subject, String text)
			throws com.rio.spring.aws.MailException {
		return messageId;
	}
}
