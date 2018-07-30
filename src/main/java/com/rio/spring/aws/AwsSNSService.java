package com.rio.spring.aws;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.AuthorizationErrorException;
import com.amazonaws.services.sns.model.InvalidParameterException;
import com.amazonaws.services.sns.model.InvalidParameterValueException;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;

/**
 * Sends SMS using the AWS Simple Notification Service.
 * 
 * @author rioortizr
 *
 */
@Service
public class AwsSNSService implements SMSService
{
    private static final Logger LOGGER = LoggerFactory.getLogger( AwsSNSService.class );

    @Value( value = "${aws.sns.region}" )
    private String              region;

    /**
     * Create instance of AmazonSNS using AWS Default credentials.
     * 
     * @return AmazonSNS Client for accessing Amazon SNS.
     */
    public AmazonSNS getSNSClient()
    {
        return AmazonSNSClient.builder().withRegion( region )
                .withCredentials( DefaultAWSCredentialsProviderChain.getInstance() ).build();
    }

    /**
     * Uses the publish method of the AmazonSNSClient class to send a message
     * directly to a phone number:
     * 
     * @param phoneNumber
     *            The recipient number. Should have country code as prefix.
     * @param message
     *            The message to send.
     * 
     * @return message Id The message identification
     */
    @Override
    public String sendSMS( final String phoneNumber, final String message )
    {
        final AmazonSNS snsClient = getSNSClient();

        LOGGER.info( "Sending SMS... MESSAGE: {} RECIPIENT: {}", message, phoneNumber );

        try
        {
            final PublishResult result = snsClient.publish(
                    new PublishRequest().withMessage( message ).withPhoneNumber( phoneNumber ) );

            final String messageId = result.getMessageId();

            LOGGER.info( "Message successfully sent to: {} with messageId: {}, request id: {} ",
                    phoneNumber, messageId, result.getSdkResponseMetadata().getRequestId() );
            return messageId;
        }
        catch ( InvalidParameterException | InvalidParameterValueException
                | AuthorizationErrorException e )
        {
            LOGGER.error( "Failed to send SMS to: " + phoneNumber + " with message: " + message );
            LOGGER.error( "Error encountered while sending SMS: " + e.getMessage() );
        }

        return null;
    }

}
