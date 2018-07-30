package com.rio.spring.aws;

import java.io.Serializable;
import java.util.Date;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * @author rioortizr
 *
 */
@SuppressWarnings( "serial" )
public class SimpleMailMessage implements MailMessage, Serializable
{
    private String   from;

    private String   replyTo;

    private String[] to;

    private String[] cc;

    private String[] bcc;

    private Date     sentDate;

    private String   subject;

    private String   text;

    /**
     * Create a new {@code SimpleMailMessage}.
     */
    public SimpleMailMessage()
    {
    }

    /**
     * Construct a new {@code SimpleMailMessage}.
     * 
     * @param from
     *            Sender
     * @param replyTo
     *            Sender's email
     * @param to
     *            Recipient's email
     * @param cc
     *            CC
     * @param bcc
     *            BCC
     * @param sentDate
     *            Email date sent
     * @param subject
     *            Email Subject
     * @param text
     *            Email Body
     */
    public SimpleMailMessage( final String from,
                              final String replyTo,
                              final String[] to,
                              final String[] cc,
                              final String[] bcc,
                              final Date sentDate,
                              final String subject,
                              final String text )
    {
        this.from = from;
        this.replyTo = replyTo;
        this.to = to;
        this.cc = cc;
        this.bcc = bcc;
        this.sentDate = sentDate;
        this.subject = subject;
        this.text = text;
    }

    /**
     * Copy constructor for creating a new {@code SimpleMailMessage} from the
     * state of an existing {@code SimpleMailMessage} instance.
     * 
     * @param original
     *            SimpleMailMessage.
     * 
     * @throws IllegalArgumentException
     *             if the supplied message is {@code null}
     */
    public SimpleMailMessage( final SimpleMailMessage original )
    {
        Assert.notNull( original, "The 'original' message argument cannot be null" );
        this.from = original.getFrom();
        this.replyTo = original.getReplyTo();
        if ( original.getTo() != null )
        {
            this.to = copy( original.getTo() );
        }
        if ( original.getCc() != null )
        {
            this.cc = copy( original.getCc() );
        }
        if ( original.getBcc() != null )
        {
            this.bcc = copy( original.getBcc() );
        }
        this.sentDate = original.getSentDate();
        this.subject = original.getSubject();
        this.text = original.getText();
    }

    @Override
    public void setFrom( final String from )
    {
        this.from = from;
    }

    public String getFrom()
    {
        return this.from;
    }

    @Override
    public void setReplyTo( final String replyTo )
    {
        this.replyTo = replyTo;
    }

    public String getReplyTo()
    {
        return replyTo;
    }

    @Override
    public void setTo( final String to )
    {
        this.to = new String[] { to };
    }

    @Override
    public void setTo( final String[] to )
    {
        this.to = to;
    }

    public String[] getTo()
    {
        return this.to;
    }

    @Override
    public void setCc( final String cc )
    {
        this.cc = new String[] { cc };
    }

    @Override
    public void setCc( final String[] cc )
    {
        this.cc = cc;
    }

    public String[] getCc()
    {
        return cc;
    }

    @Override
    public void setBcc( final String bcc )
    {
        this.bcc = new String[] { bcc };
    }

    @Override
    public void setBcc( final String[] bcc )
    {
        this.bcc = bcc;
    }

    public String[] getBcc()
    {
        return bcc;
    }

    @Override
    public void setSentDate( final Date sentDate )
    {
        this.sentDate = sentDate;
    }

    public Date getSentDate()
    {
        return sentDate;
    }

    @Override
    public void setSubject( final String subject )
    {
        this.subject = subject;
    }

    public String getSubject()
    {
        return this.subject;
    }

    @Override
    public void setText( final String text )
    {
        this.text = text;
    }

    public String getText()
    {
        return this.text;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder( "SimpleMailMessage: " );
        sb.append( "from=" ).append( this.from ).append( "; " );
        sb.append( "replyTo=" ).append( this.replyTo ).append( "; " );
        sb.append( "to=" ).append( StringUtils.arrayToCommaDelimitedString( this.to ) )
                .append( "; " );
        sb.append( "cc=" ).append( StringUtils.arrayToCommaDelimitedString( this.cc ) )
                .append( "; " );
        sb.append( "bcc=" ).append( StringUtils.arrayToCommaDelimitedString( this.bcc ) )
                .append( "; " );
        sb.append( "sentDate=" ).append( this.sentDate ).append( "; " );
        sb.append( "subject=" ).append( this.subject ).append( "; " );
        sb.append( "text=" ).append( this.text );
        return sb.toString();
    }

    private static String[] copy( String[] state )
    {
        String[] copy = new String[ state.length ];
        System.arraycopy( state, 0, copy, 0, state.length );
        return copy;
    }
}
