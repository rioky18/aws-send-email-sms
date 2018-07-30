package com.rio.spring.aws;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceAsyncClientBuilder;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import com.amazonaws.services.simpleemail.model.SendEmailResult;

/**
 * Email Service implementation to send emails with the Amazon Simple Email
 * Service.
 * 
 * @author rioortizr
 *
 */
@Service
public class AwsSESEmailService implements EmailService
{
    private static final Logger LOGGER = LoggerFactory.getLogger( AwsSESEmailService.class );

    @Value( value = "${aws.ses.region}" )
    private String              region;

    @Value( value = "${aws.ses.returnPath}" )
    private String              returnPathEmail;

    /**
     * Create instance of AmazonSimpleEmailService using AWS Default
     * credentials.
     * 
     * @return AmazonSimpleEmailService
     */
    protected AmazonSimpleEmailService getEmailService()
    {
        return AmazonSimpleEmailServiceAsyncClientBuilder.standard().withRegion( region )
                .withCredentials( DefaultAWSCredentialsProviderChain.getInstance() ).build();
    }

    /**
     *
     * Sends simple email based on input data, and then immediately queues the
     * message for sending.
     * 
     * @param from
     *            Sender email address
     * @param to
     *            Recipient email address
     * @param subject
     *            Email Subject
     * @param text
     *            Email Body
     * 
     * @return message Id returned from SendEmail action.
     */
    @Override
    public String send( String from, String[] to, String subject, String text ) throws MailException
    {
        return send( new SimpleMailMessage( from, null, to, null, null, null, subject, text ) );
    }

    /**
     * 
     * Composes an email message based on input data, and then immediately
     * queues the message for sending.
     * 
     * @param email
     *            SimpleMailMessage model that contains data such as the from,
     *            to, cc, subject, and text fields.
     * @return message Id returned from SendEmail action.
     */
    @Override
    public String send( final SimpleMailMessage email ) throws MailException
    {
        final Map<Object, Exception> failedMessages = new HashMap<>();

        try
        {
            final AmazonSimpleEmailService emailService = getEmailService();
            final SendEmailResult result = emailService.sendEmail( prepareMessage( email ) );

            LOGGER.info( "Message with id: {} successfully sent, request id: {}",
                    result.getMessageId(),
                    result.getSdkResponseMetadata().getRequestId() );

            return result.getMessageId();
        }
        catch ( AmazonClientException e )
        {
            // Ignore Exception because we are collecting and throwing all
            // if any
            // noinspection ThrowableResultOfMethodCallIgnored
            failedMessages.put( email, e );
        }

        if ( !failedMessages.isEmpty() )
        {
            throw new MailException( failedMessages );
        }

        return null;
    }

    /**
     * Contructs SendEmailRequest for sending to Amazon SES.
     * 
     * @param simpleMailMessage
     *            SimpleMailMessage message to be sent.
     * @param isHtml
     *            <code>true</code> if content of the message is in HTML format.
     * @return SendEmailRequest to be sent as a single formatted email using
     *         Amazon SES.
     */
    private SendEmailRequest prepareMessage( final SimpleMailMessage simpleMailMessage )
    {
        final Destination destination = new Destination();
        destination.withToAddresses( simpleMailMessage.getTo() );

        if ( simpleMailMessage.getCc() != null )
        {
            destination.withCcAddresses( simpleMailMessage.getCc() );
        }

        if ( simpleMailMessage.getBcc() != null )
        {
            destination.withBccAddresses( simpleMailMessage.getBcc() );
        }

        final Content subject = new Content( simpleMailMessage.getSubject() );
        Body body = null;
        // Include a body in HTML formats.
        body = new Body().withHtml( new Content( simpleMailMessage.getText() ) );

        if ( LOGGER.isDebugEnabled() )
        {
            LOGGER.debug( "Sending email with body: {}", body.toString() );
        }

        return new SendEmailRequest().withSource( simpleMailMessage.getFrom() )
                .withDestination( destination )
                .withMessage( new Message( subject, body ) ).withReturnPath( returnPathEmail );
    }

}
