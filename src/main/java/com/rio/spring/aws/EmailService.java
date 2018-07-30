package com.rio.spring.aws;

/**
 * @author rioortizr
 *
 */
public interface EmailService
{
    String send( final SimpleMailMessage email ) throws MailException;

    String send( final String from, final String[] to, final String subject, final String text )
            throws MailException;
}
