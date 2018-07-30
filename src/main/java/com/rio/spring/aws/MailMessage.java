package com.rio.spring.aws;

import java.util.Date;

public interface MailMessage
{

    public void setFrom( String from ) throws MailException;

    public void setReplyTo( String replyTo ) throws MailException;

    public void setTo( String to ) throws MailException;

    public void setTo( String[] to ) throws MailException;

    public void setCc( String cc ) throws MailException;

    public void setCc( String[] cc ) throws MailException;

    public void setBcc( String bcc ) throws MailException;

    public void setBcc( String[] bcc ) throws MailException;

    public void setSentDate( Date sentDate ) throws MailException;

    public void setSubject( String subject ) throws MailException;

    public void setText( String text ) throws MailException;
}
