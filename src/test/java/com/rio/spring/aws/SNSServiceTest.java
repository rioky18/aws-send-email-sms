package com.rio.spring.aws;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import com.amazonaws.ResponseMetadata;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.rio.spring.aws.AwsSNSService;

/**
 * @author rioortizr
 *
 */
public class SNSServiceTest extends AbstractServiceTest
{
    private AwsSNSService     smsSender;

    private AmazonSNS      snsClient;
    
    private PublishResult  mockPublishResult;
    
    @Before
    public void setup()
    {
        smsSender = Mockito.spy( AwsSNSService.class );
        snsClient = Mockito.mock( AmazonSNS.class );
        Mockito.doReturn( snsClient ).when( smsSender ).getSNSClient();
        
        ResponseMetadata metaData = Mockito.spy( new ResponseMetadata( new HashMap<String,String>() ) );
        Mockito.doReturn( "mock-request-id" ).when( metaData ).getRequestId();

        mockPublishResult = new PublishResult().withMessageId( "123456" );
        mockPublishResult.setSdkResponseMetadata( metaData ) ;
    }

    @Test
    public void testSendSimpleSMS()
    {
        String message = "This is a test SMS";
        String phone = "+6585220764";

        ArgumentCaptor<PublishRequest> publishRequest = ArgumentCaptor
                .forClass( PublishRequest.class );
        when( snsClient.publish( publishRequest.capture() ) )
                .thenReturn( mockPublishResult );
        String messageId = smsSender.sendSMS( phone, message );

        PublishRequest req = publishRequest.getValue();
        assertEquals( message, req.getMessage() );
        assertEquals( phone, req.getPhoneNumber() );
        assertNotNull( messageId );
    }

    @Test
    public void testSendSMSWithTemplate()
    {
        String template = "Hello! I'm John Doe\nThis is a test SMS message.";
        String phone = "+6585220764";

        ArgumentCaptor<PublishRequest> publishRequest = ArgumentCaptor
                .forClass( PublishRequest.class );
        when( snsClient.publish( publishRequest.capture() ) )
                .thenReturn( mockPublishResult );

        smsSender.sendSMS( phone, template );

        PublishRequest req = publishRequest.getValue();

        assertTrue( req.getMessage().contains( "John Doe" ) );
    }
}
